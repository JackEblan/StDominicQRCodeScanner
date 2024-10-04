package st.dominic.qrcodescanner.feature.management

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import st.dominic.qrcodescanner.core.designsystem.component.ShimmerImage
import st.dominic.qrcodescanner.core.model.Book
import st.dominic.qrcodescanner.core.model.BookStatus

@Composable
fun ManagementRoute(
    modifier: Modifier = Modifier, viewModel: ManagementViewModel = hiltViewModel(),
    onBookClick: (id: String) -> Unit,
) {
    val managementUiState = viewModel.managementUiState.collectAsStateWithLifecycle().value

    ManagementScreen(
        modifier = modifier,
        managementUiState = managementUiState,
        onBookClick = onBookClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagementScreen(
    modifier: Modifier,
    managementUiState: ManagementUiState,
    onBookClick: (id: String) -> Unit,
) {
    val topAppBarScrollBehavior = enterAlwaysScrollBehavior()

    Scaffold(topBar = {
        LargeTopAppBar(
            title = {
                Text(
                    text = "Management",
                    style = MaterialTheme.typography.titleLarge,
                )
            },
            scrollBehavior = topAppBarScrollBehavior,
        )
    }) { paddingValues ->
        Box(
            modifier = modifier
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues),
        ) {
            when (managementUiState) {
                ManagementUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is ManagementUiState.Success -> {
                    if (managementUiState.books.isNotEmpty()) {
                        SuccessState(
                            managementUiState = managementUiState, onBookClick = onBookClick
                        )
                    } else {
                        EmptyState()
                    }
                }

                ManagementUiState.Failed -> EmptyState()
            }
        }
    }
}

@Composable
private fun SuccessState(
    modifier: Modifier = Modifier,
    managementUiState: ManagementUiState.Success,
    onBookClick: (id: String) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(300.dp),
        modifier = modifier.fillMaxSize(),
    ) {
        items(managementUiState.books) { book ->
            BookItem(
                book = book, onBookClick = onBookClick
            )
        }
    }

}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(50.dp),
            imageVector = Icons.Default.Book,
            contentDescription = ""
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "No books found!", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "We are not busy!")
    }
}

@Composable
private fun BookItem(
    modifier: Modifier = Modifier,
    book: Book,
    onBookClick: (id: String) -> Unit,
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        onClick = {
            if (book.bookStatus == BookStatus.Borrowed) {
                onBookClick(book.id)
            }
        },
    ) {
        ShimmerImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            model = book.imageUrl,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            BookText(title = "Title", subtitle = book.title)

            BookText(title = "Author", subtitle = book.author)

            BookText(title = "Date Borrowed", subtitle = book.dateBorrowed ?: "Invalid date")

            if (book.bookStatus == BookStatus.Returned) {
                BookText(
                    title = "Date Returned", subtitle = book.dateReturned ?: "Invalid date"
                )
            }

            BookText(
                title = "Student Name", subtitle = book.studentName
            )

            BookText(title = "Book Status", subtitle = book.bookStatus.name)
        }
    }
}

@Composable
private fun BookText(title: String, subtitle: String) {
    Text(text = title, style = MaterialTheme.typography.bodyLarge)

    Spacer(modifier = Modifier.height(10.dp))

    Text(text = subtitle, style = MaterialTheme.typography.titleLarge)

    Spacer(modifier = Modifier.height(15.dp))
}