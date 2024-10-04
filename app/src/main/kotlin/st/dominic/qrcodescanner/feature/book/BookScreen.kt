package st.dominic.qrcodescanner.feature.book

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import st.dominic.qrcodescanner.core.designsystem.component.ShimmerImage
import st.dominic.qrcodescanner.core.model.Book
import st.dominic.qrcodescanner.core.model.BookStatus

@Composable
fun BookRoute(
    modifier: Modifier = Modifier, bookViewModel: BookViewModel = hiltViewModel(),
    onBookClick: (id: String) -> Unit,
) {
    val bookUiState = bookViewModel.bookUiState.collectAsStateWithLifecycle().value

    val isAdmin = bookViewModel.isAdmin.collectAsStateWithLifecycle().value

    BookScreen(
        modifier = modifier,
        bookUiState = bookUiState,
        isAdmin = isAdmin,
        onBookClick = onBookClick,
    )
}

@Composable
fun BookScreen(
    modifier: Modifier,
    bookUiState: BookUiState,
    isAdmin: Boolean?,
    onBookClick: (id: String) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        when (bookUiState) {
            BookUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is BookUiState.Success -> {
                if (bookUiState.books.isNotEmpty()) {
                    SuccessState(
                        bookUiState = bookUiState, isAdmin = isAdmin, onBookClick = onBookClick
                    )
                } else {
                    EmptyState()
                }
            }

            BookUiState.Failed -> EmptyState()
        }
    }
}

@Composable
private fun SuccessState(
    modifier: Modifier = Modifier,
    bookUiState: BookUiState.Success,
    isAdmin: Boolean?,
    onBookClick: (id: String) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(300.dp),
        modifier = modifier.fillMaxSize(),
    ) {
        items(bookUiState.books) { book ->
            BookItem(
                book = book, isAdmin = isAdmin, onBookClick = onBookClick
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

        Text(text = "Please sign in an account or borrow a book first")
    }
}

@Composable
private fun BookItem(
    modifier: Modifier = Modifier,
    book: Book,
    isAdmin: Boolean?,
    onBookClick: (id: String) -> Unit,
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        onClick = {
            if (isAdmin == true && book.bookStatus == BookStatus.Borrowed) {
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

            if (isAdmin == true) {
                BookText(
                    title = "Student Name", subtitle = book.studentName
                )
            }

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