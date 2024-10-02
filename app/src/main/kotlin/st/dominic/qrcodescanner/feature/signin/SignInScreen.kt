package st.dominic.qrcodescanner.feature.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import st.dominic.qrcodescanner.R

@Composable
fun SignInRoute(
    modifier: Modifier = Modifier, viewModel: SignInViewModel = hiltViewModel(),
    onSignInSuccess: () -> Unit,
    onCreateAccount: () -> Unit,
) {
    val signInResult = viewModel.signInResult.collectAsStateWithLifecycle().value

    val signInErrorMessage = viewModel.signInErrorMessage.collectAsStateWithLifecycle().value

    LaunchedEffect(key1 = signInResult) {
        if (signInResult == true) {
            onSignInSuccess()
        }
    }

    SignInScreen(
        modifier = modifier,
        signInErrorMessage = signInErrorMessage,
        onLogin = viewModel::signInWithEmailAndPassword,
        onCreateAccount = onCreateAccount
    )
}

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    signInErrorMessage: String?,
    onLogin: (email: String, password: String) -> Unit,
    onCreateAccount: () -> Unit,
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
        ) {
            Image(
                modifier = Modifier.size(50.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_placeholder),
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
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
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
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )

            Button(onClick = {
                if (signInState.validateFields()) {
                    onLogin(signInState.email, signInState.password)
                } else {
                    scope.launch { snackbarHostState.showSnackbar(message = "We cannot process your request!") }
                }
            }) {
                Text(text = "Login")
            }

            Button(onClick = onCreateAccount) {
                Text(text = "Create Account")
            }
        }
    }
}