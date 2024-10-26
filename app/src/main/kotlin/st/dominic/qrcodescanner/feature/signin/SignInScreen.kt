package st.dominic.qrcodescanner.feature.signin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

@Composable
fun SignInRoute(
    modifier: Modifier = Modifier, viewModel: SignInViewModel = hiltViewModel(),
    onSignUp: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    val signInUiState = viewModel.signInUiState.collectAsStateWithLifecycle().value

    val snackbar = viewModel.snackbar.collectAsStateWithLifecycle().value

    val sendPasswordResetEmailResult =
        viewModel.sendPasswordResetEmailResult.collectAsStateWithLifecycle().value

    val navigateUp = viewModel.navigateUp.collectAsStateWithLifecycle().value

    SignInScreen(
        modifier = modifier,
        signInUiState = signInUiState,
        snackbar = snackbar,
        navigateUp = navigateUp,
        sendPasswordResetEmailResult = sendPasswordResetEmailResult,
        onSignIn = viewModel::signInWithEmailAndPassword,
        onSignUp = onSignUp,
        onSendPasswordResetEmail = viewModel::sendPasswordResetEmail,
        onNavigateUp = onNavigateUp,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    signInUiState: SignInUiState,
    snackbar: String?,
    navigateUp: Boolean?,
    sendPasswordResetEmailResult: Boolean?,
    onSignIn: (email: String, password: String) -> Unit,
    onSignUp: () -> Unit,
    onSendPasswordResetEmail: (email: String) -> Unit,
    onNavigateUp: () -> Unit,
) {
    val signInState = rememberSignInState()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = snackbar) {
        if (snackbar != null) {
            snackbarHostState.showSnackbar(message = snackbar)
        }
    }

    LaunchedEffect(key1 = navigateUp) {
        if (navigateUp != null) {
            onNavigateUp()
        }
    }

    LaunchedEffect(key1 = sendPasswordResetEmailResult) {
        if (sendPasswordResetEmailResult != null) {
            snackbarHostState.showSnackbar(message = "Password reset link has been sent to your email!")
        }
    }

    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }, topBar = {
        TopAppBar(title = {
            Text(text = "Sign In")
        })
    }) { paddingValues ->
        Box(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .consumeWindowInsets(paddingValues),
        ) {
            when (signInUiState) {
                SignInUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                SignInUiState.SignIn -> {
                    SignIn(modifier = modifier,
                           signInState = signInState,
                           onSignUp = onSignUp,
                           onSendPasswordResetEmail = onSendPasswordResetEmail,
                           onSignIn = onSignIn,
                           onShowSnackbar = { message ->
                               scope.launch { snackbarHostState.showSnackbar(message = message) }
                           })
                }
            }
        }
    }
}

@Composable
private fun SignIn(
    modifier: Modifier,
    signInState: SignInState,
    onSignUp: () -> Unit,
    onSendPasswordResetEmail: (email: String) -> Unit,
    onSignIn: (email: String, password: String) -> Unit,
    onShowSnackbar: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp),
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = signInState.email,
            onValueChange = {
                signInState.email = it
            },
            label = {
                Text(text = "Email")
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next, keyboardType = KeyboardType.Email
            ),
        )

        Spacer(modifier = Modifier.height(5.dp))

        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                          value = signInState.password,
                          onValueChange = {
                              signInState.password = it
                          },
                          label = {
                              Text(text = "Password")
                          },
                          keyboardOptions = KeyboardOptions(
                              imeAction = ImeAction.Next, keyboardType = KeyboardType.Email
                          ),
                          visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(5.dp))

        Button(onClick = {
            if (signInState.validateFields()) {
                onSignIn(signInState.email, signInState.password)
            } else {
                onShowSnackbar("We cannot process your request!")
            }
        }) {
            Text(text = "Login")
        }

        Button(onClick = onSignUp) {
            Text(text = "Sign Up")
        }

        Button(onClick = {
            if (signInState.email.isBlank()) {
                onShowSnackbar("We cannot process your request!")
            } else {
                onSendPasswordResetEmail(signInState.email)
            }
        }) {
            Text(text = "Reset Password")
        }
    }
}