package st.dominic.qrcodescanner.feature.admin

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import st.dominic.qrcodescanner.R
import st.dominic.qrcodescanner.core.designsystem.component.ShimmerImage
import st.dominic.qrcodescanner.core.model.Book

@Composable
fun AdminRoute(
    modifier: Modifier = Modifier,
    viewModel: AdminViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
) {
    val adminUiState = viewModel.adminUiState.collectAsStateWithLifecycle().value

    AdminScreen(
        modifier = modifier,
        adminUiState = adminUiState,
        onReturnBook = viewModel::returnBook,
        onNavigateUp = onNavigateUp,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    adminUiState: AdminUiState?,
    onReturnBook: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val topAppBarScrollBehavior = enterAlwaysScrollBehavior()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    Scaffold(topBar = {
        LargeTopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.return_book),
                    style = MaterialTheme.typography.titleLarge,
                )
            },
            scrollBehavior = topAppBarScrollBehavior,
        )
    }, floatingActionButton = {
        FloatingActionButton(onClick = onReturnBook) {
            Icon(imageVector = Icons.Default.Book, contentDescription = "")
        }

    }, snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }) { paddingValues ->
        Box(
            modifier = modifier.fillMaxSize(),
        ) {
            when (adminUiState) {
                AdminUiState.Loading, null -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is AdminUiState.Success -> {
                    if (adminUiState.book != null) {
                        SuccessState(
                            topAppBarScrollBehavior = topAppBarScrollBehavior,
                            scrollState = scrollState,
                            paddingValues = paddingValues,
                            book = adminUiState.book,
                        )
                    }
                }

                AdminUiState.Returned -> onNavigateUp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuccessState(
    modifier: Modifier = Modifier,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    scrollState: ScrollState,
    paddingValues: PaddingValues,
    book: Book,
) {
    Column(
        modifier = modifier
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(paddingValues)
            .consumeWindowInsets(paddingValues)
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