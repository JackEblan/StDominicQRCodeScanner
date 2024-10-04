package st.dominic.qrcodescanner.feature.borrowbook.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import st.dominic.qrcodescanner.feature.borrowbook.BorrowBookRoute

fun NavController.navigateToBorrowBookScreen() {
    navigate(BorrowBookRouteData)
}

fun NavGraphBuilder.borrowBookScreen(onNavigateUp: () -> Unit) {
    composable<BorrowBookRouteData> {
        BorrowBookRoute(onNavigateUp = onNavigateUp)
    }
}