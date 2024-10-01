package st.dominic.qrcodescanner.feature.book.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import st.dominic.qrcodescanner.feature.book.BookRoute

fun NavController.navigateToBookScreen() {
    navigate(BookRouteData) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

fun NavGraphBuilder.bookScreen(onBorrowBook: () -> Unit, onSignIn: () -> Unit) {
    composable<BookRouteData> {
        BookRoute(onBorrowBook = onBorrowBook, onSignIn = onSignIn)
    }
}