package st.dominic.qrcodescanner.feature.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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

    SignInScreen(
        modifier = modifier,
        signInUiState = signInUiState,
        signInErrorMessage = signInErrorMessage,
        onSignIn = viewModel::signInWithEmailAndPassword,
        onSignUp = onSignUp,
        onSignInSuccess = onSignInSuccess
    )
}

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    signInUiState: SignInUiState?,
    signInErrorMessage: String?,
    onSignIn: (email: String, password: String) -> Unit,
    onSignUp: () -> Unit,
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

    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
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
                           onSignIn = onSignIn,
                           onSignInError = {
                               scope.launch { snackbarHostState.showSnackbar(message = "We cannot process your request!") }
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
    onSignIn: (email: String, password: String) -> Unit,
    onSignInError: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.CenterHorizontally),
            imageVector = Icons.Default.Book,
            contentDescription = ""
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
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

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick = onSignUp) {
                Text(text = "Sign Up")
            }

            Button(onClick = {
                if (signInState.validateFields()) {
                    onSignIn(signInState.email, signInState.password)
                } else {
                    onSignInError()
                }
            }) {
                Text(text = "Login")
            }
        }
    }
}