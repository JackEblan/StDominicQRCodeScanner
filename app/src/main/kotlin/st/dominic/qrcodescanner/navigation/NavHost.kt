package st.dominic.qrcodescanner.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import st.dominic.qrcodescanner.feature.borrowbook.navigation.navigateToBorrowBookScreen
import st.dominic.qrcodescanner.feature.management.navigation.navigateToManagementScreen
import st.dominic.qrcodescanner.feature.returnbook.navigation.navigateToReturnBookScreen
import st.dominic.qrcodescanner.feature.signup.navigation.navigateToSignUp

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
        startDestination = BookNavigationRoute::class,
    ) {
        bookNavGraph(
            snackbarHostState = snackbarHostState,
            onFloatingActionButtonClick = navController::navigateToBorrowBookScreen,
            onSignIn = navController::navigateToAccountNavGraph,
            onManagement = navController::navigateToManagementScreen,
            onNavigateUp = navController::navigateUp,
            onShowSnackBar = { message ->
                scope.launch {
                    snackbarHostState.showSnackbar(message = message)
                }
            },
            onBookClick = navController::navigateToReturnBookScreen
        )

        accountNavGraph(
            onSignUp = navController::navigateToSignUp,
            onNavigateUp = navController::navigateToBookNavGraph
        )
    }
}