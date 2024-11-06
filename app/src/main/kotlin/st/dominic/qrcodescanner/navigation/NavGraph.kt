package st.dominic.qrcodescanner.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import st.dominic.qrcodescanner.feature.book.navigation.BookRouteData
import st.dominic.qrcodescanner.feature.book.navigation.bookScreen
import st.dominic.qrcodescanner.feature.book.navigation.navigateToBookScreen
import st.dominic.qrcodescanner.feature.borrowbook.navigation.borrowBookScreen
import st.dominic.qrcodescanner.feature.home.navigation.HomeRouteData
import st.dominic.qrcodescanner.feature.home.navigation.homeScreen
import st.dominic.qrcodescanner.feature.management.navigation.managementScreen
import st.dominic.qrcodescanner.feature.profile.navigation.navigateToProfileScreen
import st.dominic.qrcodescanner.feature.profile.navigation.profileScreen
import st.dominic.qrcodescanner.feature.returnbook.navigation.returnBookScreen
import st.dominic.qrcodescanner.feature.signin.navigation.SignInRouteData
import st.dominic.qrcodescanner.feature.signin.navigation.signInScreen
import st.dominic.qrcodescanner.feature.signup.navigation.signUpScreen

fun NavGraphBuilder.accountNavGraph(onSignUp: () -> Unit, onNavigateUp: () -> Unit) {
    navigation<AccountNavigationRoute>(startDestination = SignInRouteData) {
        signInScreen(
            onSignUp = onSignUp,
            onNavigateUp = onNavigateUp,
        )

        signUpScreen(onNavigateUp = onNavigateUp)
    }
}

fun NavGraphBuilder.bookNavGraph(
    snackbarHostState: SnackbarHostState,
    onFloatingActionButtonClick: () -> Unit,
    onSignIn: () -> Unit,
    onManagement: () -> Unit,
    onNavigateUp: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    onBookClick: (String) -> Unit,
) {
    navigation<BookNavigationRoute>(startDestination = HomeRouteData) {
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
            onFloatingActionButtonClick = onFloatingActionButtonClick,
            builder = {
                bookScreen()

                profileScreen(
                    onSignIn = onSignIn,
                    onManagement = onManagement,
                    onShowSnackBar = onShowSnackBar
                )
            },
        )

        borrowBookScreen(onNavigateUp = onNavigateUp)

        returnBookScreen(onNavigateUp = onNavigateUp)

        managementScreen(onBookClick = onBookClick)
    }
}

fun NavController.navigateToAccountNavGraph() {
    navigate(AccountNavigationRoute) {
        popUpTo(BookNavigationRoute) {
            inclusive = true
        }
    }
}

fun NavController.navigateToBookNavGraph() {
    navigate(BookNavigationRoute) {
        popUpTo(AccountNavigationRoute) {
            inclusive = true
        }
    }
}

@Serializable
data object AccountNavigationRoute

@Serializable
data object BookNavigationRoute