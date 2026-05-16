package io.github.alimsrepo.flowtab.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Visual style of the selection indicator shown on the active navigation item.
 *
 * Pass one of the three subtypes to [NavConfig.navIndicator]. All color properties default to
 * [Color.Unspecified], which causes the renderer to fall back to the appropriate [NavColor]
 * value automatically — so the indicator works correctly out of the box without any color
 * argument.
 *
 * | Subtype | Default color fallback |
 * |---------|----------------------|
 * | [Ripple] | [NavColor.selectedRippleColor] |
 * | [Dot]    | [NavColor.selectedIconColor]   |
 * | [Line]   | [NavColor.selectedIconColor]   |
 *
 * @property indicatorPadding Inset applied around the indicator shape.
 *
 * @see NavConfig.navIndicator
 * @see NavColor
 */
sealed class NavIndicator(open val indicatorPadding: Dp) {

    /**
     * A pill-shaped background that fills the full width of the selected item.
     *
     * Best suited for bold, high-contrast designs where the active state needs to be
     * immediately obvious.
     *
     * @property color Background fill of the ripple. Defaults to [NavColor.selectedRippleColor]
     *   when [Color.Unspecified] (the default).
     * @property indicatorPadding Vertical and horizontal inset of the pill. Default: `4.dp`.
     *
     * Example:
     * ```kotlin
     * NavIndicator.Ripple(
     *     color = MaterialTheme.colorScheme.primaryContainer,
     *     indicatorPadding = 6.dp
     * )
     * ```
     */
    data class Ripple(
        val color: Color = Color.Unspecified,
        override val indicatorPadding: Dp = 4.dp,
    ) : NavIndicator(indicatorPadding)

    /**
     * A small circle drawn below the active item's icon.
     *
     * Best suited for minimal designs — especially when labels are hidden — inspired by
     * apps like Instagram.
     *
     * @property size Diameter of the dot. Default: `8.dp`.
     * @property color Fill color of the dot. Defaults to [NavColor.selectedIconColor]
     *   when [Color.Unspecified] (the default).
     * @property indicatorPadding Gap between the dot and the bottom edge of the bar. Default: `4.dp`.
     *
     * Example:
     * ```kotlin
     * NavIndicator.Dot(
     *     size = 6.dp,
     *     color = MaterialTheme.colorScheme.primary
     * )
     * ```
     */
    data class Dot(
        val size: Dp = 8.dp,
        val color: Color = Color.Unspecified,
        override val indicatorPadding: Dp = 4.dp,
    ) : NavIndicator(indicatorPadding)

    /**
     * A short horizontal bar drawn below the active item's icon.
     *
     * Follows the Material Design 3 navigation bar convention and works well in
     * professional or corporate design systems.
     *
     * @property height Thickness of the line. Default: `2.dp`.
     * @property width Length of the line. Default: `40.dp`.
     * @property color Color of the line. Defaults to [NavColor.selectedIconColor]
     *   when [Color.Unspecified] (the default).
     * @property indicatorPadding Gap between the line and the bottom edge of the bar. Default: `4.dp`.
     *
     * Example:
     * ```kotlin
     * NavIndicator.Line(
     *     height = 3.dp,
     *     width = 36.dp,
     *     color = MaterialTheme.colorScheme.primary
     * )
     * ```
     */
    data class Line(
        val height: Dp = 2.dp,
        val width: Dp = 40.dp,
        val color: Color = Color.Unspecified,
        override val indicatorPadding: Dp = 4.dp,
    ) : NavIndicator(indicatorPadding)
}