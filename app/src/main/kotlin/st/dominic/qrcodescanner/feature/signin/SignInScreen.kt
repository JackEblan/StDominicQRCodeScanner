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
    onSignInSuccess: () -> Unit,
    onSignUp: () -> Unit,
) {
    val signInUiState = viewModel.signInUiState.collectAsStateWithLifecycle().value

    val signInErrorMessage = viewModel.signInErrorMessage.collectAsStateWithLifecycle().value

    val emailVerificationResult =
        viewModel.emailVerificationResult.collectAsStateWithLifecycle().value

    val emailVerificationErrorMessage =
        viewModel.emailVerificationErrorMessage.collectAsStateWithLifecycle().value

    SignInScreen(
        modifier = modifier,
        signInUiState = signInUiState,
        signInErrorMessage = signInErrorMessage,
        emailVerificationResult = emailVerificationResult,
        emailVerificationErrorMessage = emailVerificationErrorMessage,
        onSignIn = viewModel::signInWithEmailAndPassword,
        onSignUp = onSignUp,
        onVerifyEmail = viewModel::verifyEmail,
        onResetPassword = {},
        onSignInSuccess = onSignInSuccess,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    signInUiState: SignInUiState?,
    signInErrorMessage: String?,
    emailVerificationResult: Boolean?,
    emailVerificationErrorMessage: String?,
    onSignIn: (email: String, password: String) -> Unit,
    onSignUp: () -> Unit,
    onVerifyEmail: () -> Unit,
    onResetPassword: () -> Unit,
    onSignInSuccess: () -> Unit,
) {
    val signInState = rememberSignInState()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = signInErrorMessage) {
        if (signInErrorMessage != null) {
            snackbarHostState.showSnackbar(message = signInErrorMessage)
        }
    }

    LaunchedEffect(key1 = emailVerificationResult) {
        if (emailVerificationResult != null) {
            snackbarHostState.showSnackbar(message = "Email verification link has been sent to your email!")
        }
    }

    LaunchedEffect(key1 = emailVerificationErrorMessage) {
        if (emailVerificationErrorMessage != null) {
            snackbarHostState.showSnackbar(message = emailVerificationErrorMessage)
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
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues),
        ) {
            when (signInUiState) {
                SignInUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is SignInUiState.Success -> onSignInSuccess()

                null -> {
                    SignIn(modifier = modifier,
                           signInState = signInState,
                           onSignUp = onSignUp,
                           onVerifyEmail = onVerifyEmail,
                           onResetPassword = onResetPassword,
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
    onVerifyEmail: () -> Unit,
    onResetPassword: () -> Unit,
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
                onShowSnackbar("Email is empty!")
            } else {
                onVerifyEmail()
            }
        }) {
            Text(text = "Verify Email")
        }

        Button(onClick = {
            if (signInState.email.isBlank()) {
                onShowSnackbar("Email is empty!")
            } else {
                onResetPassword()
            }
        }) {
            Text(text = "Reset Password")
        }
    }
}