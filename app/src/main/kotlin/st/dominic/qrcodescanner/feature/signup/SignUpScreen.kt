package st.dominic.qrcodescanner.feature.signup

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
fun SignUpRoute(
    modifier: Modifier = Modifier, viewModel: SignUpViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
) {
    val signUpUiState = viewModel.signUpUiState.collectAsStateWithLifecycle().value

    val snackbar = viewModel.snackbar.collectAsStateWithLifecycle().value

    val navigateUp = viewModel.navigateUp.collectAsStateWithLifecycle().value

    SignUpScreen(
        modifier = modifier,
        signUpUiState = signUpUiState,
        snackbar = snackbar,
        navigateUp = navigateUp,
        onSignUp = viewModel::createUserWithEmailAndPassword,
        onNavigateUp = onNavigateUp,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    signUpUiState: SignUpUiState,
    snackbar: String?,
    navigateUp: Boolean?,
    onSignUp: (name: String, email: String, password: String) -> Unit,
    onNavigateUp: () -> Unit,
) {
    val signUpState = rememberSignUpState()

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

    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }, topBar = {
        TopAppBar(title = {
            Text(text = "Sign Up")
        })
    }) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues),
        ) {
            when (signUpUiState) {
                SignUpUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                SignUpUiState.SignUp -> {
                    SignUp(modifier = modifier,
                           signUpState = signUpState,
                           onSignUp = onSignUp,
                           onSignUpError = {
                               scope.launch { snackbarHostState.showSnackbar(message = "We cannot process your request!") }
                           })
                }
            }
        }
    }
}

@Composable
private fun SignUp(
    modifier: Modifier,
    signUpState: SignUpState,
    onSignUp: (name: String, email: String, password: String) -> Unit,
    onSignUpError: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp),
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = signUpState.name,
            onValueChange = {
                signUpState.name = it
            },
            label = {
                Text(text = "Name")
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
            ),
        )

        Spacer(modifier = Modifier.height(5.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = signUpState.email,
            onValueChange = {
                signUpState.email = it
            },
            label = {
                Text(text = "Email")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next, keyboardType = KeyboardType.Email
            ),
        )

        Spacer(modifier = Modifier.height(5.dp))

        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                          value = signUpState.password,
                          onValueChange = {
                              signUpState.password = it
                          },
                          label = {
                              Text(text = "Password")
                          },
                          keyboardOptions = KeyboardOptions(
                              imeAction = ImeAction.Next, keyboardType = KeyboardType.Password
                          ),
                          visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(5.dp))

        Button(onClick = {
            if (signUpState.validateFields()) {
                onSignUp(signUpState.name, signUpState.email, signUpState.password)
            } else {
                onSignUpError()
            }
        }) {
            Text(text = "Sign Up")
        }
    }
}