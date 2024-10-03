package st.dominic.qrcodescanner.feature.signin.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import st.dominic.qrcodescanner.feature.signin.SignInRoute

fun NavController.navigateToSignIn() {
    navigate(SignInRouteData) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

fun NavGraphBuilder.signInScreen(
    onSignInSuccess: () -> Unit, onSignUp: () -> Unit
) {
    composable<SignInRouteData> {
        SignInRoute(
            onSignInSuccess = onSignInSuccess, onSignUp = onSignUp
        )
    }
}