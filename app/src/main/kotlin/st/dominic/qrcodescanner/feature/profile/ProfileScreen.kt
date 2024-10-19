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
) {
    val profileUiState = viewModel.profileUiState.collectAsStateWithLifecycle().value

    ProfileScreen(modifier = modifier,
                  profileUiState = profileUiState,
                  onSignIn = onSignIn,
                  onManagement = onManagement,
                  onSignOut = {
                      viewModel.signOut()
                      onSignIn()
                  })
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileUiState: ProfileUiState?,
    onSignIn: () -> Unit,
    onManagement: () -> Unit,
    onSignOut: () -> Unit,
) {
    when (profileUiState) {
        ProfileUiState.EmailVerify -> {
            EmailVerify(modifier = modifier, onSignOut = onSignOut)
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
    modifier: Modifier = Modifier, onSignOut: () -> Unit,
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