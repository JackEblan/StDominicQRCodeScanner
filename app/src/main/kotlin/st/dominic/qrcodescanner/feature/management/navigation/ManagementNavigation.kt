package st.dominic.qrcodescanner.feature.management.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import st.dominic.qrcodescanner.feature.management.ManagementRoute

fun NavController.navigateToManagementScreen() {
    navigate(ManagementRouteData)
}

fun NavGraphBuilder.managementScreen(onBookClick: (String) -> Unit) {
    composable<ManagementRouteData> {
        ManagementRoute(onBookClick = onBookClick)
    }
}