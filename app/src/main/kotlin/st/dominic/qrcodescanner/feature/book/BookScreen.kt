package st.dominic.qrcodescanner.feature.book

import androidx.compose.foundation.layout.Arrangement
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
) {
    val profileUiState = bookViewModel.profileUiState.collectAsStateWithLifecycle().value

    val bookUiState = bookViewModel.bookUiState.collectAsStateWithLifecycle().value

    BookScreen(
        modifier = modifier,
        profileUiState = profileUiState,
        bookUiState = bookUiState,
    )
}

@Composable
fun BookScreen(
    modifier: Modifier,
    profileUiState: ProfileUiState?,
    bookUiState: BookUiState?,
) {
    when (profileUiState) {
        ProfileUiState.Loading, null -> {
            LoadingState(modifier = modifier, text = "Checking User Data")
        }

        is ProfileUiState.Success -> {
            BookState(modifier = modifier, bookUiState = bookUiState)
        }

        ProfileUiState.EmailVerify -> {
            EmptyState(
                modifier = modifier,
                title = "Email not verified!",
                subtitle = "Please verify your email first!"
            )
        }

        ProfileUiState.Failed -> {
            EmptyState(
                modifier = modifier,
                title = "Sign in an account!",
                subtitle = "Please sign in an account first!"
            )
        }
    }

}

@Composable
private fun LoadingState(modifier: Modifier = Modifier, text: String) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = text)
    }
}

@Composable
private fun BookState(modifier: Modifier = Modifier, bookUiState: BookUiState?) {
    when (bookUiState) {
        BookUiState.Loading, null -> {
            LoadingState(modifier = modifier, text = "Fetching Books")
        }

        is BookUiState.Success -> {
            if (bookUiState.books.isNotEmpty()) {
                BookSuccessState(modifier = modifier, books = bookUiState.books)
            } else {
                EmptyState(
                    modifier = modifier,
                    title = "No books found!",
                    subtitle = "Borrow your first book"
                )
            }
        }
    }
}

@Composable
private fun BookSuccessState(
    modifier: Modifier = Modifier,
    books: List<Book>,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(300.dp),
        modifier = modifier.fillMaxSize(),
    ) {
        items(books) { book ->
            BookItem(
                book = book,
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier, title: String, subtitle: String) {
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

        Text(text = title, style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = subtitle)
    }
}

@Composable
private fun BookItem(
    modifier: Modifier = Modifier,
    book: Book,
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
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