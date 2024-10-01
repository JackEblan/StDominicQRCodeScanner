package st.dominic.qrcodescanner.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import st.dominic.qrcodescanner.feature.book.navigation.BookRouteData
import st.dominic.qrcodescanner.feature.book.navigation.bookScreen
import st.dominic.qrcodescanner.feature.borrow.navigation.borrowScreen
import st.dominic.qrcodescanner.feature.borrow.navigation.navigateToBorrowScreen
import st.dominic.qrcodescanner.feature.signin.navigation.navigateToSignIn
import st.dominic.qrcodescanner.feature.signin.navigation.signInScreen
import st.dominic.qrcodescanner.feature.signup.navigation.navigateToSignUp
import st.dominic.qrcodescanner.feature.signup.navigation.signUpScreen

@Composable
fun StDominicQrCodeScannerNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = BookRouteData::class,
    ) {

        bookScreen(
            onBorrowBook = navController::navigateToBorrowScreen,
            onSignIn = navController::navigateToSignIn
        )

        borrowScreen(onNavigateUp = navController::navigateUp)

        signInScreen(
            onSignInSuccess = navController::navigateUp,
            onCreateAccount = navController::navigateToSignUp
        )

        signUpScreen(onSignUpSuccess = navController::navigateUp)
    }
}