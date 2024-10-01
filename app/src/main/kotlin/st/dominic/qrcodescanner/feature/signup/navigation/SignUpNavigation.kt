package st.dominic.qrcodescanner.feature.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import st.dominic.qrcodescanner.feature.signup.SignUpRoute

fun NavController.navigateToSignUp() {
    navigate(SignUpRouteData) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

fun NavGraphBuilder.signUpScreen(
    onSignUpSuccess: () -> Unit
) {
    composable<SignUpRouteData> {
        SignUpRoute(onSignUpSuccess = onSignUpSuccess)
    }
}