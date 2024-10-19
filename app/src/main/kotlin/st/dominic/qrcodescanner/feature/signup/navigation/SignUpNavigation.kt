package st.dominic.qrcodescanner.feature.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import st.dominic.qrcodescanner.feature.signup.SignUpRoute

fun NavController.navigateToSignUp() {
    navigate(SignUpRouteData)
}

fun NavGraphBuilder.signUpScreen(
    onNavigateUp: () -> Unit
) {
    composable<SignUpRouteData> {
        SignUpRoute(onNavigateUp = onNavigateUp)
    }
}