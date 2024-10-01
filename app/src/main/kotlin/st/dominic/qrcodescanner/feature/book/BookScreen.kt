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
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import st.dominic.qrcodescanner.R
import st.dominic.qrcodescanner.core.designsystem.component.ShimmerImage
import st.dominic.qrcodescanner.core.model.AuthCurrentUser
import st.dominic.qrcodescanner.core.model.Book
import st.dominic.qrcodescanner.core.model.BookStatus

@Composable
fun BookRoute(
    modifier: Modifier = Modifier, bookViewModel: BookViewModel = hiltViewModel(),
    onBorrowBook: () -> Unit,
    onSignIn: () -> Unit,
) {
    val authCurrentUser = bookViewModel.authCurrentUser

    val bookUiState = bookViewModel.bookUiState.collectAsStateWithLifecycle().value

    BookScreen(
        modifier = modifier,
        bookUiState = bookUiState,
        authCurrentUser = authCurrentUser,
        onBorrowBook = onBorrowBook,
        onSignIn = onSignIn,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BookScreen(
    modifier: Modifier, bookUiState: BookUiState,
    authCurrentUser: AuthCurrentUser?,
    onBorrowBook: () -> Unit,
    onSignIn: () -> Unit,
) {
    val topAppBarScrollBehavior = enterAlwaysScrollBehavior()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(key1 = true) {
        if (authCurrentUser == null) {
            val result = snackbarHostState.showSnackbar(
                message = "You are currently not signed in!", actionLabel = "Sign in",
                duration = SnackbarDuration.Indefinite,
            )

            when (result) {
                SnackbarResult.Dismissed -> Unit
                SnackbarResult.ActionPerformed -> onSignIn()
            }
        }
    }

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
    }, snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
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
                    SuccessState(
                        bookUiState = bookUiState, authCurrentUser = authCurrentUser
                    )
                }
            }
        }

    }
}

@Composable
fun SuccessState(
    modifier: Modifier = Modifier,
    bookUiState: BookUiState.Success,
    authCurrentUser: AuthCurrentUser?,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(300.dp),
        modifier = modifier.fillMaxSize(),
    ) {
        if (authCurrentUser != null) {
            item(span = StaggeredGridItemSpan.FullLine) {
                CurrentUserItem(authCurrentUser = authCurrentUser)
            }
        }

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
            LabeledText(title = "Title", subtitle = book.title)

            LabeledText(title = "Author", subtitle = book.author)

            LabeledText(title = "Date Borrowed", subtitle = book.dateBorrowed ?: "Invalid date")

            if (book.bookStatus == BookStatus.Returned) {
                LabeledText(
                    title = "Date Returned", subtitle = book.dateReturned ?: "Invalid date"
                )
            }
        }
    }
}

@Composable
private fun CurrentUserItem(modifier: Modifier = Modifier, authCurrentUser: AuthCurrentUser) {
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
            model = "",
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            Text(text = "Welcome User!", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(20.dp))

            LabeledText(title = "User ID", subtitle = authCurrentUser.uid.toString())

            LabeledText(title = "Email", subtitle = authCurrentUser.email.toString())

            Button(onClick = {}) {
                Text(text = "Sign Out")
            }
        }
    }
}

@Composable
private fun LabeledText(title: String, subtitle: String) {
    Text(text = title, style = MaterialTheme.typography.bodyLarge)

    Spacer(modifier = Modifier.height(10.dp))

    Text(text = subtitle, style = MaterialTheme.typography.titleLarge)

    Spacer(modifier = Modifier.height(15.dp))
}