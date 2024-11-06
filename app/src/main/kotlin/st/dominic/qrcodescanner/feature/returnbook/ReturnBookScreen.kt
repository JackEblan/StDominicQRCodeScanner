package st.dominic.qrcodescanner.feature.returnbook

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import st.dominic.qrcodescanner.R
import st.dominic.qrcodescanner.core.designsystem.component.ShimmerImage
import st.dominic.qrcodescanner.core.model.Book

@Composable
fun ReturnBookRoute(
    modifier: Modifier = Modifier,
    viewModel: ReturnBookViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
) {
    val adminUiState = viewModel.adminUiState.collectAsStateWithLifecycle().value

    val qrCodeResult = viewModel.qrCodeResult.collectAsStateWithLifecycle().value

    val qrCodeInstallError = viewModel.qrCodeError.collectAsStateWithLifecycle().value

    val moduleInstallProgress = viewModel.moduleInstallProgress.collectAsStateWithLifecycle().value

    ReturnBookScreen(
        modifier = modifier,
        returnBookUiState = adminUiState,
        onStartScan = viewModel::isScannerModuleAvailable,
        qrCodeResultState = qrCodeResult,
        qrCodeInstallErrorState = qrCodeInstallError,
        moduleInstallProgress = moduleInstallProgress,
        onReturnBook = viewModel::returnBook,
        onNavigateUp = onNavigateUp,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReturnBookScreen(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    returnBookUiState: ReturnBookUiState?,
    qrCodeResultState: String?,
    qrCodeInstallErrorState: String?,
    moduleInstallProgress: Float?,
    onStartScan: () -> Unit,
    onReturnBook: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = moduleInstallProgress) {
        if (moduleInstallProgress != null && moduleInstallProgress < 1f) {
            snackbarHostState.showSnackbar(
                message = "Installing QR Code Libraries!", duration = SnackbarDuration.Indefinite
            )
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.return_book),
                    style = MaterialTheme.typography.titleLarge,
                )
            },
        )
    }, floatingActionButton = {
        FloatingActionButton(onClick = onStartScan) {
            Icon(imageVector = Icons.Default.QrCode, contentDescription = "")
        }

    }, snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }) { paddingValues ->
        Box(
            modifier = modifier.fillMaxSize(),
        ) {
            when (returnBookUiState) {
                ReturnBookUiState.Loading, null -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is ReturnBookUiState.Success -> {
                    if (returnBookUiState.book != null) {
                        SuccessState(scrollState = scrollState,
                                     paddingValues = paddingValues,
                                     book = returnBookUiState.book,
                                     qrCodeResult = qrCodeResultState,
                                     qrCodeInstallError = qrCodeInstallErrorState,
                                     onReturnBook = onReturnBook,
                                     onShowSnackBar = { message ->
                                         scope.launch {
                                             snackbarHostState.showSnackbar(message = message)
                                         }
                                     })
                    }
                }

                ReturnBookUiState.Returned -> onNavigateUp()
            }
        }
    }
}

@Composable
private fun SuccessState(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    paddingValues: PaddingValues,
    book: Book,
    qrCodeResult: String?,
    qrCodeInstallError: String?,
    onReturnBook: () -> Unit,
    onShowSnackBar: (String) -> Unit,
) {
    LaunchedEffect(key1 = qrCodeResult) {
        qrCodeResult?.let { qrCode ->
            if (qrCode == book.qrCode) {
                onReturnBook()
            } else {
                onShowSnackBar("QR Codes does not match!")
            }
        }
    }

    LaunchedEffect(key1 = qrCodeInstallError) {
        if (qrCodeInstallError != null) {
            onShowSnackBar(qrCodeInstallError)
        }
    }

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(paddingValues)
            .consumeWindowInsets(paddingValues)
    ) {
        ShimmerImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(10.dp)
                .clip(RoundedCornerShape(4.dp)),
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