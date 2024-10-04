package st.dominic.qrcodescanner.feature.profile

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import st.dominic.qrcodescanner.core.model.AuthCurrentUser

@Composable
fun ProfileRoute(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    onSignIn: () -> Unit,
    onManagement: () -> Unit,
) {
    val isAdmin = viewModel.isAdmin.collectAsStateWithLifecycle().value

    ProfileScreen(modifier = modifier,
                  authCurrentUser = viewModel.authCurrentUser,
                  isAdmin = isAdmin,
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
    authCurrentUser: AuthCurrentUser?,
    isAdmin: Boolean?,
    onSignIn: () -> Unit,
    onManagement: () -> Unit,
    onSignOut: () -> Unit,
) {
    if (authCurrentUser != null) {
        Profile(
            modifier = modifier,
            authCurrentUser = authCurrentUser,
            isAdmin = isAdmin,
            onManagement = onManagement,
            onSignOut = onSignOut
        )
    } else {
        SignIn(modifier = modifier, onSignIn = onSignIn)
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
            text = "You are currently not signed in!", style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = onSignIn) {
            Text(text = "Sign In")
        }
    }
}

@Composable
private fun Profile(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    authCurrentUser: AuthCurrentUser,
    isAdmin: Boolean?,
    onManagement: () -> Unit,
    onSignOut: () -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(10.dp),
    ) {
        ProfileText(title = "Name", subtitle = authCurrentUser.displayName)

        ProfileText(title = "User ID", subtitle = authCurrentUser.uid)

        ProfileText(title = "Email", subtitle = authCurrentUser.email)

        if (isAdmin == true) {
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