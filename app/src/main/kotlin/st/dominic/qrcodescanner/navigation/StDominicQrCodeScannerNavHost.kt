package st.dominic.qrcodescanner.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import st.dominic.qrcodescanner.feature.book.navigation.BookRouteData
import st.dominic.qrcodescanner.feature.book.navigation.bookScreen
import st.dominic.qrcodescanner.feature.book.navigation.navigateToBookScreen
import st.dominic.qrcodescanner.feature.borrowbook.navigation.borrowBookScreen
import st.dominic.qrcodescanner.feature.borrowbook.navigation.navigateToBorrowBookScreen
import st.dominic.qrcodescanner.feature.home.navigation.HomeRouteData
import st.dominic.qrcodescanner.feature.home.navigation.homeScreen
import st.dominic.qrcodescanner.feature.management.navigation.managementScreen
import st.dominic.qrcodescanner.feature.management.navigation.navigateToManagementScreen
import st.dominic.qrcodescanner.feature.profile.navigation.navigateToProfileScreen
import st.dominic.qrcodescanner.feature.profile.navigation.profileScreen
import st.dominic.qrcodescanner.feature.returnbook.navigation.navigateToReturnBookScreen
import st.dominic.qrcodescanner.feature.returnbook.navigation.returnBookScreen
import st.dominic.qrcodescanner.feature.signin.navigation.navigateToSignInScreen
import st.dominic.qrcodescanner.feature.signin.navigation.signInScreen
import st.dominic.qrcodescanner.feature.signup.navigation.navigateToSignUp
import st.dominic.qrcodescanner.feature.signup.navigation.signUpScreen

@Composable
fun StDominicQrCodeScannerNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val scope = rememberCoroutineScope()

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
            onFloatingActionButtonClick = navController::navigateToBorrowBookScreen,
            builder = {
                bookScreen()

                profileScreen(onSignIn = navController::navigateToSignInScreen,
                              onManagement = navController::navigateToManagementScreen,
                              onShowSnackBar = { message ->
                                  scope.launch {
                                      snackbarHostState.showSnackbar(message = message)
                                  }
                              })
            },
        )

        borrowBookScreen(onNavigateUp = navController::navigateUp)

        signInScreen(
            onSignUp = navController::navigateToSignUp,
            onNavigateUp = navController::navigateUp,
        )

        signUpScreen(onNavigateUp = navController::navigateUp)

        returnBookScreen(onNavigateUp = navController::navigateUp)

        managementScreen(onBookClick = navController::navigateToReturnBookScreen)
    }
}