package st.dominic.qrcodescanner.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProfileRoute(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    onSignIn: () -> Unit,
    onManagement: () -> Unit,
    onShowSnackBar: (String) -> Unit,
) {
    val profileUiState = viewModel.profileUiState.collectAsStateWithLifecycle().value

    val snackbar = viewModel.snackbar.collectAsStateWithLifecycle().value

    val emailVerificationResult =
        viewModel.emailVerificationResult.collectAsStateWithLifecycle().value

    ProfileScreen(
        modifier = modifier,
        profileUiState = profileUiState,
        snackbar = snackbar,
        emailVerificationResult = emailVerificationResult,
        onSignIn = onSignIn,
        onVerify = viewModel::verifyEmail,
        onManagement = onManagement,
        onSignOut = {
            viewModel.signOut()
            onSignIn()
        },
        onShowSnackBar = onShowSnackBar,
    )
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileUiState: ProfileUiState?,
    snackbar: String?,
    emailVerificationResult: Boolean?,
    onSignIn: () -> Unit,
    onVerify: () -> Unit,
    onManagement: () -> Unit,
    onSignOut: () -> Unit,
    onShowSnackBar: (String) -> Unit,
) {

    LaunchedEffect(key1 = snackbar) {
        if (snackbar != null) {
            onShowSnackBar(snackbar)
        }
    }

    LaunchedEffect(key1 = emailVerificationResult) {
        if (emailVerificationResult != null) {
            onShowSnackBar("Email verification link has been sent to your email!")
        }
    }

    when (profileUiState) {
        ProfileUiState.EmailVerify -> {
            EmailVerify(modifier = modifier, onVerify = onVerify, onSignOut = onSignOut)
        }

        ProfileUiState.Failed -> {
            SignIn(modifier = modifier, onSignIn = onSignIn)
        }

        ProfileUiState.Loading, null -> {
            LoadingState(modifier = modifier)
        }

        is ProfileUiState.Success -> {
            Profile(
                modifier = modifier,
                profileUiState = profileUiState,
                onManagement = onManagement,
                onSignOut = onSignOut
            )
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun SignIn(
    modifier: Modifier = Modifier, onSignIn: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(50.dp),
            imageVector = Icons.Default.Book,
            contentDescription = ""
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "You are not signed in!", style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = onSignIn) {
            Text(text = "Sign In")
        }
    }
}

@Composable
private fun EmailVerify(
    modifier: Modifier = Modifier, onVerify: () -> Unit, onSignOut: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(50.dp),
            imageVector = Icons.Default.Book,
            contentDescription = ""
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Please verify your email address", style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = onVerify) {
            Text(text = "Verify Email")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = onSignOut) {
            Text(text = "Sign Out")
        }
    }
}

@Composable
private fun Profile(
    modifier: Modifier = Modifier,
    profileUiState: ProfileUiState.Success,
    onManagement: () -> Unit,
    onSignOut: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        ProfileText(title = "Name", subtitle = profileUiState.authCurrentUser.displayName)

        ProfileText(title = "User ID", subtitle = profileUiState.authCurrentUser.uid)

        ProfileText(title = "Email", subtitle = profileUiState.authCurrentUser.email)

        if (profileUiState.isAdmin) {
            Button(onClick = onManagement) {
                Text(text = "Management")
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        Button(onClick = onSignOut) {
            Text(text = "Sign Out")
        }
    }
}

@Composable
private fun ProfileText(title: String, subtitle: String) {
    Text(text = title, style = MaterialTheme.typography.bodyLarge)

    Spacer(modifier = Modifier.height(10.dp))

    Text(text = subtitle, style = MaterialTheme.typography.titleLarge)

    Spacer(modifier = Modifier.height(15.dp))
}