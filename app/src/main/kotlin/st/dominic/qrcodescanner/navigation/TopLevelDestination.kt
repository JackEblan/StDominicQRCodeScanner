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
package st.dominic.qrcodescanner.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Book
import androidx.compose.ui.graphics.vector.ImageVector
import st.dominic.qrcodescanner.R
import st.dominic.qrcodescanner.feature.book.navigation.BookRouteData
import st.dominic.qrcodescanner.feature.home.navigation.HomeDestination
import st.dominic.qrcodescanner.feature.profile.navigation.ProfileRouteData
import kotlin.reflect.KClass

enum class TopLevelDestination(
    override val label: Int,
    override val icon: ImageVector,
    override val contentDescription: Int,
    override val route: KClass<*>,
) : HomeDestination {
    BOOK(
        label = R.string.book,
        icon = Icons.Default.Book,
        contentDescription = R.string.book,
        route = BookRouteData::class,
    ),

    PROFILE(
        label = R.string.profile,
        icon = Icons.Default.AccountBox,
        contentDescription = R.string.profile,
        route = ProfileRouteData::class,
    ),
}
