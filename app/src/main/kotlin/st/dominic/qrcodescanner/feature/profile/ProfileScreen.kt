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
import st.dominic.qrcodescanner.core.model.GetProfileResult

@Composable
fun ProfileRoute(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    onSignIn: () -> Unit,
    onManagement: () -> Unit,
) {
    val getProfileResult = viewModel.profile.collectAsStateWithLifecycle().value

    ProfileScreen(modifier = modifier,
                  getProfileResult = getProfileResult,
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
    getProfileResult: GetProfileResult?,
    onSignIn: () -> Unit,
    onManagement: () -> Unit,
    onSignOut: () -> Unit,
) {
    when {
        getProfileResult == null -> {
            Loading()
        }

        getProfileResult.isSignedIn == false -> {
            SignIn(modifier = modifier, onSignIn = onSignIn)
        }

        getProfileResult.isEmailVerified == false -> {
            VerifyEmail(modifier = modifier, onSignOut = onSignOut)
        }

        else -> {
            Profile(
                modifier = modifier,
                getProfileResult = getProfileResult,
                onManagement = onManagement,
                onSignOut = onSignOut
            )
        }
    }
}

@Composable
private fun Loading(modifier: Modifier = Modifier) {
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
private fun VerifyEmail(
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
    getProfileResult: GetProfileResult,
    onManagement: () -> Unit,
    onSignOut: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        ProfileText(title = "Name", subtitle = getProfileResult.displayName!!)

        ProfileText(title = "User ID", subtitle = getProfileResult.uid!!)

        ProfileText(title = "Email", subtitle = getProfileResult.email!!)

        if (getProfileResult.isAdmin!!) {
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