package st.dominic.qrcodescanner.feature.admin.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import st.dominic.qrcodescanner.feature.admin.AdminRoute

fun NavController.navigateToAdminScreen(id: String) {
    navigate(AdminRouteData(id = id)) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

fun NavGraphBuilder.adminScreen(onNavigateUp: () -> Unit) {
    composable<AdminRouteData> {
        AdminRoute(onNavigateUp = onNavigateUp)
    }
}