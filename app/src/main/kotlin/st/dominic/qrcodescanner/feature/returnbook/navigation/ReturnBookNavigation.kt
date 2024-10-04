package st.dominic.qrcodescanner.feature.returnbook.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import st.dominic.qrcodescanner.feature.returnbook.ReturnBookRoute

fun NavController.navigateToReturnBookScreen(id: String) {
    navigate(ReturnBookRouteData(id = id))
}

fun NavGraphBuilder.returnBookScreen(onNavigateUp: () -> Unit) {
    composable<ReturnBookRouteData> {
        ReturnBookRoute(onNavigateUp = onNavigateUp)
    }
}