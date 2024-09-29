package st.dominic.qrcodescanner.feature.book

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import st.dominic.qrcodescanner.R
import st.dominic.qrcodescanner.core.designsystem.component.ShimmerImage
import st.dominic.qrcodescanner.core.model.Book
import st.dominic.qrcodescanner.core.model.BookStatus

@Composable
fun BookRoute(
    modifier: Modifier = Modifier, bookViewModel: BookViewModel = hiltViewModel(),
    onBorrowBook: () -> Unit,
) {


    val bookUiState = bookViewModel.bookUiState.collectAsStateWithLifecycle().value

    BookScreen(
        modifier = modifier,
        bookUiState = bookUiState,
        onBorrowBook = onBorrowBook,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BookScreen(
    modifier: Modifier, bookUiState: BookUiState,
    onBorrowBook: () -> Unit,
) {
    val topAppBarScrollBehavior = enterAlwaysScrollBehavior()

    Scaffold(topBar = {
        LargeTopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                )
            },
            scrollBehavior = topAppBarScrollBehavior,
        )
    }, floatingActionButton = {
        FloatingActionButton(onClick = onBorrowBook) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "")
        }
    }) { paddingValues ->
        Box(
            modifier = modifier
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues),
        ) {
            when (bookUiState) {
                BookUiState.Loading -> {

                }

                is BookUiState.Success -> {
                    SuccessState(bookUiState = bookUiState)
                }
            }
        }

    }
}

@Composable
fun SuccessState(modifier: Modifier = Modifier, bookUiState: BookUiState.Success) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(300.dp),
        modifier = modifier.fillMaxSize(),
    ) {
        items(bookUiState.books) { book ->
            BookItem(book = book)
        }
    }

}

@Composable
private fun BookItem(modifier: Modifier = Modifier, book: Book) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        onClick = {},
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
            BookItemText(title = "Title", subtitle = book.title)

            BookItemText(title = "Author", subtitle = book.author)

            BookItemText(title = "Date Borrowed", subtitle = book.dateBorrowed ?: "Invalid date")

            if (book.bookStatus == BookStatus.Returned) {
                BookItemText(
                    title = "Date Returned",
                    subtitle = book.dateReturned ?: "Invalid date"
                )
            }
        }
    }
}

@Composable
private fun BookItemText(title: String, subtitle: String) {
    Text(text = title, style = MaterialTheme.typography.bodyLarge)

    Spacer(modifier = Modifier.height(10.dp))

    Text(text = subtitle, style = MaterialTheme.typography.titleLarge)

    Spacer(modifier = Modifier.height(15.dp))
}