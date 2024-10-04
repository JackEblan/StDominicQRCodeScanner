package st.dominic.qrcodescanner.feature.borrowbook

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import st.dominic.qrcodescanner.R
import st.dominic.qrcodescanner.core.designsystem.component.ShimmerImage
import st.dominic.qrcodescanner.core.model.BorrowBookResult
import st.dominic.qrcodescanner.core.model.LocalBook

@Composable
fun BorrowBookRoute(
    modifier: Modifier = Modifier,
    viewModel: BorrowBookViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
) {
    val addBookResultState = viewModel.borrowBookResultState.collectAsStateWithLifecycle().value

    val qrCodeResultState = viewModel.qrCodeResultState.collectAsStateWithLifecycle().value

    val qrCodeInstallError = viewModel.qrCodeErrorState.collectAsStateWithLifecycle().value

    val moduleInstallProgress = viewModel.moduleInstallProgress.collectAsStateWithLifecycle().value

    BorrowBookScreen(
        modifier = modifier,
        borrowBookResultState = addBookResultState,
        qrCodeResult = qrCodeResultState,
        qrCodeInstallError = qrCodeInstallError,
        moduleInstallProgress = moduleInstallProgress,
        onBorrowBook = viewModel::borrowBook,
        onStartScan = viewModel::isScannerModuleAvailable,
        onNavigateUp = onNavigateUp,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BorrowBookScreen(
    modifier: Modifier = Modifier, scrollState: ScrollState = rememberScrollState(),
    borrowBookResultState: BorrowBookResult?,
    qrCodeResult: String?,
    qrCodeInstallError: String?,
    moduleInstallProgress: Float?,
    onBorrowBook: (LocalBook) -> Unit,
    onStartScan: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    val topAppBarScrollBehavior = enterAlwaysScrollBehavior()

    val borrowBookState = rememberBorrowBookState()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()

    val pickImage = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            borrowBookState.imageUri = uri
        }
    }

    val animatedProgress by animateFloatAsState(
        targetValue = moduleInstallProgress ?: 0f,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "animatedProgress"
    )

    LaunchedEffect(key1 = borrowBookResultState) {
        if (borrowBookResultState?.exception != null) {
            snackbarHostState.showSnackbar(message = borrowBookResultState.exception.message.toString())
        }

        if (borrowBookResultState?.notSignedIn == true) {
            snackbarHostState.showSnackbar(message = "Please sign in an account first!")
        }

        if (borrowBookResultState?.success == true) {
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
        if (moduleInstallProgress != null) {
            snackbarHostState.showSnackbar(
                message = "Installing QR Code Libraries!", duration = SnackbarDuration.Indefinite
            )
        }
    }

    Scaffold(topBar = {
        LargeTopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.borrow_book),
                    style = MaterialTheme.typography.titleLarge,
                )
            },
            scrollBehavior = topAppBarScrollBehavior,
        )
    }, floatingActionButton = {
        FloatingActionButton(onClick = {
            val localBook = borrowBookState.buildLocalBook()

            if (borrowBookResultState?.progress == null && localBook != null) {
                onBorrowBook(localBook)
            } else {
                scope.launch { snackbarHostState.showSnackbar(message = "We cannot process your request!") }
            }
        }) {
            Icon(imageVector = Icons.Default.Upload, contentDescription = "")
        }

    }, snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }) { paddingValues ->
        Column(
            modifier = modifier
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
        ) {
            if (moduleInstallProgress != null) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), progress = {
                    animatedProgress
                })
            }

            ImagePicker(imageUri = borrowBookState.imageUri,
                        progress = borrowBookResultState?.progress,
                        onPickImage = {
                            pickImage.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                        })

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

@Composable
private fun ImagePicker(
    modifier: Modifier = Modifier,
    imageUri: Uri,
    progress: Float?,
    onPickImage: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        ShimmerImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clickable {
                    onPickImage()
                },
            model = imageUri,
        )

        if (progress != null) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.BottomEnd)
                    .padding(10.dp),
                strokeWidth = 2.dp,
            )
        }
    }
}