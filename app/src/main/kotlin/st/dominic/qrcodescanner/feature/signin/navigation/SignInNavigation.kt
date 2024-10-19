package st.dominic.qrcodescanner.feature.signin.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import st.dominic.qrcodescanner.feature.signin.SignInRoute

fun NavController.navigateToSignInScreen() {
    navigate(SignInRouteData)
}

fun NavGraphBuilder.signInScreen(
    onSignUp: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    composable<SignInRouteData> {
        SignInRoute(
            onSignUp = onSignUp, onNavigateUp = onNavigateUp
        )
    }
}