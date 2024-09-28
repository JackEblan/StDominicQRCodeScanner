package st.dominic.qrcodescanner.feature.borrow.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import st.dominic.qrcodescanner.feature.borrow.BorrowRoute

fun NavController.navigateToBorrowScreen() {
    navigate(BorrowRouteData) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

fun NavGraphBuilder.borrowScreen() {
    composable<BorrowRouteData> {
        BorrowRoute()
    }
}