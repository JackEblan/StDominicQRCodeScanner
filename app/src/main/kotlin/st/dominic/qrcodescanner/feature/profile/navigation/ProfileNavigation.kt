package st.dominic.qrcodescanner.feature.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import st.dominic.qrcodescanner.feature.profile.ProfileRoute

fun NavController.navigateToProfileScreen() {
    navigate(ProfileRouteData) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

fun NavGraphBuilder.profileScreen(
    onSignIn: () -> Unit,
) {
    composable<ProfileRouteData> {
        ProfileRoute(
            onSignIn = onSignIn,
        )
    }
}