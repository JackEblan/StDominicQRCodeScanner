package st.dominic.qrcodescanner.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import st.dominic.qrcodescanner.feature.book.navigation.BookRouteData
import st.dominic.qrcodescanner.feature.book.navigation.bookScreen
import st.dominic.qrcodescanner.feature.borrow.navigation.borrowScreen
import st.dominic.qrcodescanner.feature.borrow.navigation.navigateToBorrowScreen

@Composable
fun StDominicQrCodeScannerNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = BookRouteData::class,
    ) {

        bookScreen(onBorrowBook = navController::navigateToBorrowScreen)

        borrowScreen(onNavigateUp = navController::navigateUp)
    }
}