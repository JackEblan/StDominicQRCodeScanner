/*
 *
 *   Copyright 2023 Einstein Blanco
 *
 *   Licensed under the GNU General Public License v3.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.gnu.org/licenses/gpl-3.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package st.dominic.qrcodescanner.feature.home

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import st.dominic.qrcodescanner.core.model.GetProfileResult
import st.dominic.qrcodescanner.feature.home.navigation.HomeDestination
import kotlin.reflect.KClass

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    navController: NavHostController = rememberNavController(),
    topLevelDestinations: List<HomeDestination>,
    startDestination: KClass<*>,
    onItemClick: (NavHostController, HomeDestination) -> Unit,
    onFloatingActionButtonClick: () -> Unit,
    builder: NavGraphBuilder.() -> Unit,
) {
    val profile = viewModel.profile.collectAsStateWithLifecycle().value

    HomeScreen(
        modifier = modifier,
        getProfileResult = profile,
        snackbarHostState = snackbarHostState,
        navController = navController,
        topLevelDestinations = topLevelDestinations,
        startDestination = startDestination,
        onItemClick = onItemClick,
        onFloatingActionButtonClick = onFloatingActionButtonClick,
        builder = builder,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    getProfileResult: GetProfileResult?,
    snackbarHostState: SnackbarHostState,
    navController: NavHostController = rememberNavController(),
    topLevelDestinations: List<HomeDestination>,
    startDestination: KClass<*>,
    onItemClick: (NavHostController, HomeDestination) -> Unit,
    onFloatingActionButtonClick: () -> Unit,
    builder: NavGraphBuilder.() -> Unit,
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    val topBarTitleStringResource = topLevelDestinations.find { destination ->
        currentDestination.isTopLevelDestinationInHierarchy(destination.route)
    }?.label ?: topLevelDestinations.first().label

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            topLevelDestinations.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            imageVector = destination.icon,
                            contentDescription = stringResource(id = destination.contentDescription),
                        )
                    },
                    label = { Text(stringResource(id = destination.label)) },
                    selected = currentDestination.isTopLevelDestinationInHierarchy(destination.route),
                    onClick = {
                        onItemClick(navController, destination)
                    },
                )
            }
        },
        navigationSuiteColors = NavigationSuiteDefaults.colors(navigationBarContainerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = topBarTitleStringResource),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
            )
        }, snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }, floatingActionButton = {
            if (getProfileResult?.isSignedIn == true) {
                FloatingActionButton(onClick = onFloatingActionButtonClick) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "")
                }
            }
        }) { paddingValues ->
            NavHost(
                modifier = modifier
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues),
                navController = navController,
                startDestination = startDestination,
                builder = builder,
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } ?: false
