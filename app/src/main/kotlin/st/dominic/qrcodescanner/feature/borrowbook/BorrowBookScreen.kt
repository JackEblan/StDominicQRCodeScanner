package st.dominic.qrcodescanner.feature.borrowbook

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import st.dominic.qrcodescanner.R
import st.dominic.qrcodescanner.core.designsystem.component.ShimmerImage
import st.dominic.qrcodescanner.core.model.LocalBook

@Composable
fun BorrowBookRoute(
    modifier: Modifier = Modifier,
    viewModel: BorrowBookViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
) {
    val borrowBookSuccess = viewModel.borrowBookSuccess.collectAsStateWithLifecycle().value

    val qrCodeResultState = viewModel.qrCodeResultState.collectAsStateWithLifecycle().value

    val qrCodeInstallError = viewModel.qrCodeErrorState.collectAsStateWithLifecycle().value

    val moduleInstallProgress = viewModel.moduleInstallProgress.collectAsStateWithLifecycle().value

    val bookProgress = viewModel.bookProgress.collectAsStateWithLifecycle().value

    BorrowBookScreen(
        modifier = modifier,
        borrowBookSuccess = borrowBookSuccess,
        qrCodeResult = qrCodeResultState,
        qrCodeInstallError = qrCodeInstallError,
        moduleInstallProgress = moduleInstallProgress,
        bookProgress = bookProgress,
        onBorrowBook = viewModel::borrowBook,
        onStartScan = viewModel::isScannerModuleAvailable,
        onNavigateUp = onNavigateUp,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BorrowBookScreen(
    modifier: Modifier = Modifier,
    borrowBookSuccess: Boolean?,
    qrCodeResult: String?,
    qrCodeInstallError: String?,
    moduleInstallProgress: Float?,
    bookProgress: Float?,
    onBorrowBook: (LocalBook) -> Unit,
    onStartScan: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    val borrowBookState = rememberBorrowBookState()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val pickImage = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            borrowBookState.imageUri = uri
        }
    }

    LaunchedEffect(key1 = borrowBookSuccess) {
        if (borrowBookSuccess == true) {
            onNavigateUp()
        }
    }

    LaunchedEffect(key1 = qrCodeResult) {
        if (qrCodeResult != null) {
            borrowBookState.qrCode = qrCodeResult
        }
    }

    LaunchedEffect(key1 = qrCodeInstallError) {
        if (qrCodeInstallError != null) {
            snackbarHostState.showSnackbar(message = qrCodeInstallError)
        }
    }

    LaunchedEffect(key1 = moduleInstallProgress) {
        if (moduleInstallProgress != null && moduleInstallProgress < 1f) {
            snackbarHostState.showSnackbar(
                message = "Installing QR Code Libraries!", duration = SnackbarDuration.Indefinite
            )
        }
    }

    LaunchedEffect(key1 = borrowBookState.snackbarMessage) {
        if (borrowBookState.snackbarMessage.isNotEmpty()) {
            snackbarHostState.showSnackbar(
                message = borrowBookState.snackbarMessage, duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.borrow_book),
                )
            },
        )
    }, floatingActionButton = {
        FloatingActionButton(onClick = {
            val localBook = borrowBookState.buildLocalBook()

            if (bookProgress == null && localBook != null) {
                onBorrowBook(localBook)
            }
        }) {
            Icon(imageVector = Icons.Default.Upload, contentDescription = "")
        }

    }, snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
        ) {
            if (bookProgress != null) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), progress = {
                    bookProgress
                })
            }

            ShimmerImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable {
                        pickImage.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                    },
                model = borrowBookState.imageUri,
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                value = borrowBookState.qrCode,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text(text = "QR Code Result")
                },
                trailingIcon = {
                    IconButton(onClick = onStartScan) {
                        Icon(
                            imageVector = Icons.Default.QrCode, contentDescription = ""
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                value = borrowBookState.title,
                onValueChange = {
                    borrowBookState.title = it
                },
                label = {
                    Text(text = "Title")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                value = borrowBookState.author,
                onValueChange = {
                    borrowBookState.author = it
                },
                label = {
                    Text(text = "Author")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )
        }
    }
}