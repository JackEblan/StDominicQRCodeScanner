package st.dominic.qrcodescanner.feature.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import st.dominic.qrcodescanner.R
import st.dominic.qrcodescanner.core.model.AuthCurrentUser

@Composable
fun ProfileRoute(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    onSignIn: () -> Unit,
) {
    ProfileScreen(modifier = modifier,
                  authCurrentUser = viewModel.authCurrentUser,
                  onSignIn = onSignIn,
                  onSignOut = {
                      viewModel.signOut()
                      onSignIn()
                  })
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    authCurrentUser: AuthCurrentUser?,
    onSignIn: () -> Unit,
    onSignOut: () -> Unit,
) {
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .fillMaxSize(),
    ) {
        Image(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally),
            bitmap = ImageBitmap.imageResource(R.drawable.st_domini_college),
            contentDescription = ""
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            if (authCurrentUser != null) {
                ProfileDetails(authCurrentUser = authCurrentUser, onSignOut = onSignOut)
            } else {
                SignInMessage(onSignIn = onSignIn)
            }
        }
    }
}

@Composable
private fun SignInMessage(onSignIn: () -> Unit) {
    Text(
        text = "You are currently not signed in!", style = MaterialTheme.typography.headlineSmall
    )

    Spacer(modifier = Modifier.height(20.dp))

    Button(onClick = onSignIn) {
        Text(text = "Sign In")
    }
}

@Composable
private fun ProfileDetails(authCurrentUser: AuthCurrentUser, onSignOut: () -> Unit) {
    Text(text = "Welcome User!", style = MaterialTheme.typography.headlineSmall)

    Spacer(modifier = Modifier.height(20.dp))

    ProfileText(title = "Name", subtitle = authCurrentUser.uid.toString())

    ProfileText(title = "User ID", subtitle = authCurrentUser.uid.toString())

    ProfileText(title = "Email", subtitle = authCurrentUser.email.toString())

    Button(onClick = onSignOut) {
        Text(text = "Sign Out")
    }
}

@Composable
private fun ProfileText(title: String, subtitle: String) {
    Text(text = title, style = MaterialTheme.typography.bodyLarge)

    Spacer(modifier = Modifier.height(10.dp))

    Text(text = subtitle, style = MaterialTheme.typography.titleLarge)

    Spacer(modifier = Modifier.height(15.dp))
}