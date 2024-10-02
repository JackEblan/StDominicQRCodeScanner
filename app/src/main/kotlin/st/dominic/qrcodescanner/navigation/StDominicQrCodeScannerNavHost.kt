package st.dominic.qrcodescanner.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import st.dominic.qrcodescanner.feature.book.navigation.BookRouteData
import st.dominic.qrcodescanner.feature.book.navigation.bookScreen
import st.dominic.qrcodescanner.feature.book.navigation.navigateToBookScreen
import st.dominic.qrcodescanner.feature.borrow.navigation.borrowScreen
import st.dominic.qrcodescanner.feature.borrow.navigation.navigateToBorrowScreen
import st.dominic.qrcodescanner.feature.home.navigation.HomeRouteData
import st.dominic.qrcodescanner.feature.home.navigation.homeScreen
import st.dominic.qrcodescanner.feature.profile.navigation.navigateToProfileScreen
import st.dominic.qrcodescanner.feature.profile.navigation.profileScreen
import st.dominic.qrcodescanner.feature.signin.navigation.navigateToSignIn
import st.dominic.qrcodescanner.feature.signin.navigation.signInScreen
import st.dominic.qrcodescanner.feature.signup.navigation.navigateToSignUp
import st.dominic.qrcodescanner.feature.signup.navigation.signUpScreen

@Composable
fun StDominicQrCodeScannerNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = HomeRouteData::class,
    ) {

        homeScreen(
            snackbarHostState = snackbarHostState,
            topLevelDestinations = TopLevelDestination.entries,
            startDestination = BookRouteData::class,
            onItemClick = { homeNavHostController, homeDestination ->
                when (homeDestination) {
                    TopLevelDestination.BOOK -> homeNavHostController.navigateToBookScreen()
                    TopLevelDestination.PROFILE -> homeNavHostController.navigateToProfileScreen()
                }
            },
            onFloatingActionButtonClick = navController::navigateToBorrowScreen,
            builder = {
                bookScreen()

                profileScreen(onSignIn = navController::navigateToSignIn)
            },
        )

        borrowScreen(onNavigateUp = navController::navigateUp)

        signInScreen(
            onSignInSuccess = navController::navigateUp,
            onCreateAccount = navController::navigateToSignUp
        )

        signUpScreen(onSignUpSuccess = navController::navigateUp)
    }
}