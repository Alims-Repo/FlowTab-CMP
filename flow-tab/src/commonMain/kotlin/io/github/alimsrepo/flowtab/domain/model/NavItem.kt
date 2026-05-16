package io.github.alimsrepo.flowtab.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents a single item in the bottom navigation bar.
 *
 * Each item is navigation-library-agnostic: it carries only display data and a
 * string [id] that your navigation layer can act on inside the `BottomNavigation`
 * `onItemSelected` callback.
 *
 * @property id Unique string identifier. Must be distinct across all items passed to
 *   `BottomNavigation`; duplicate IDs cause an [IllegalArgumentException] at runtime.
 * @property label Text displayed below the icon when labels are enabled via [NavConfig.showLabels].
 * @property icon Icon shown in the unselected state.
 * @property selectedIcon Icon shown in the selected state. Defaults to [icon], so you only
 *   need to provide this when you want a filled/outlined swap on selection.
 * @property type Determines the item's layout and interaction behaviour.
 *   Defaults to [NavItemType.Standard]. See [NavItemType] for all options.
 * @property badge Optional badge overlay. See [BadgeData].
 *
 * @see NavItemType
 * @see BadgeData
 *
 * Example:
 * ```kotlin
 * // Standard destination with outlined/filled icon swap and a notification badge
 * NavItem(
 *     id = "home",
 *     label = "Home",
 *     icon = Icons.Outlined.Home,
 *     selectedIcon = Icons.Filled.Home,
 *     badge = BadgeData(count = 3)
 * )
 *
 * // Expandable search bar
 * NavItem(
 *     id = "search",
 *     label = "Search",
 *     icon = Icons.Default.Search,
 *     type = NavItemType.Search
 * )
 *
 * // Isolated action button with a 45° rotated plus icon
 * NavItem(
 *     id = "create",
 *     label = "Create",
 *     icon = Icons.Default.Add,
 *     type = NavItemType.Isolated(rotation = 45f)
 * )
 * ```
 */
@Immutable
data class NavItem(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector = icon,
    val type: NavItemType = NavItemType.Standard,
    val badge: BadgeData? = null
)