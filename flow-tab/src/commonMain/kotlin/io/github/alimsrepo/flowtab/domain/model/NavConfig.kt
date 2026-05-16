package io.github.alimsrepo.flowtab.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Appearance and behaviour configuration for `BottomNavigation`.
 *
 * All parameters are optional and have sensible defaults. Construct a [NavConfig] with only
 * the values you want to override; every other property keeps its default.
 *
 * @property height Height of the navigation bar and the collapsed search/isolated buttons. Default: `60.dp`.
 * @property cornerRadius Corner radius of the bar's rounded pill shape. Default: `60.dp`.
 * @property maxWidth Maximum width the bar will expand to on wide screens. Default: `460.dp`.
 * @property iconsSize Size of every navigation icon, including the isolated button icon. Default: `20.dp`.
 * @property animationDuration Duration in milliseconds for width/height transition animations. Default: `250`.
 * @property enableBlur Whether to apply a frosted-glass blur behind the bar.
 *   Requires a non-null `hazeState` to be passed to `BottomNavigation`; if `hazeState` is `null`
 *   this flag has no effect and the bar renders as a solid surface. Default: `true`.
 * @property blurIntensity Background opacity layered on top of the blur (0f = fully transparent,
 *   1f = fully opaque). Only meaningful when [enableBlur] is `true`. Default: `0.95f`.
 * @property showLabels Whether to show text labels below icons. Default: `true`.
 * @property hideLabelsOnSearchExpand When `true`, labels are hidden while the search field is
 *   expanded to reclaim horizontal space. Default: `true`.
 * @property showBorder Whether to draw a 1dp border around the bar and action buttons. Default: `true`.
 * @property elevation Drop-shadow elevation of the bar. Default: `0.dp`.
 * @property navColor Color scheme for the bar. See [NavColor].
 * @property navIndicator Selection indicator style. See [NavIndicator]. Default: [NavIndicator.Ripple].
 * @property searchPlaceholder Placeholder text inside the expanded search field. Default: `"Search..."`.
 *
 * @see NavColor
 * @see NavIndicator
 *
 * Example:
 * ```kotlin
 * NavConfig(
 *     height = 56.dp,
 *     showLabels = false,
 *     enableBlur = false,
 *     navIndicator = NavIndicator.Dot(color = MaterialTheme.colorScheme.primary),
 *     navColor = NavColor(
 *         backgroundColor = MaterialTheme.colorScheme.surface,
 *         selectedIconColor = MaterialTheme.colorScheme.primary
 *     ),
 *     searchPlaceholder = "Search products…"
 * )
 * ```
 */
@Immutable
data class NavConfig(
    val height: Dp = 60.dp,
    val cornerRadius: Dp = 60.dp,
    val maxWidth: Dp = 460.dp,
    val iconsSize: Dp = 20.dp,
    val animationDuration: Int = 250,
    val enableBlur: Boolean = true,
    val blurIntensity: Float = 0.95f,
    val showLabels: Boolean = true,
    val hideLabelsOnSearchExpand: Boolean = true,
    val showBorder: Boolean = true,
    val elevation: Dp = 0.dp,
    val navColor: NavColor = NavColor(),
    val navIndicator: NavIndicator = NavIndicator.Ripple(),
    val searchPlaceholder: String = "Search..."
)