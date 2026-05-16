package io.github.alimsrepo.flowtab.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Color scheme for `BottomNavigation`.
 *
 * All properties have Material-like defaults that work on a white surface. Override only
 * the values that differ from your design system; every other property keeps its default.
 *
 * @property backgroundColor Background fill of the navigation bar and action buttons.
 *   Default: `Color(0xFFFFFFFF)` (white).
 * @property borderColor Color of the 1dp border drawn when [NavConfig.showBorder] is `true`.
 *   Default: `Color(0xFFE0E0E0)` (light grey).
 * @property selectedIconColor Tint applied to the icon of the active destination.
 *   Default: `Color(0xFF1A73E8)` (Google Blue).
 * @property unSelectedIconColor Tint applied to icons of inactive destinations.
 *   Default: `Color(0xFF9E9E9E)` (Grey 400).
 * @property selectedTextColor Color of the label text for the active destination.
 *   Default: `Color(0xFF1A73E8)` (Google Blue).
 * @property unSelectedTextColor Color of the label text for inactive destinations.
 *   Default: `Color(0xFF9E9E9E)` (Grey 400).
 * @property selectedRippleColor Fill color used by [NavIndicator.Ripple] for the active item.
 *   Default: `Color(0x331A73E8)` (20% Google Blue).
 *
 * @see NavConfig.navColor
 *
 * Example:
 * ```kotlin
 * // Dark-theme palette
 * NavColor(
 *     backgroundColor    = Color(0xFF1E1E1E),
 *     borderColor        = Color(0xFF2E2E2E),
 *     selectedIconColor  = Color(0xFF82B1FF),
 *     unSelectedIconColor = Color(0xFF757575),
 *     selectedTextColor  = Color(0xFF82B1FF),
 *     unSelectedTextColor = Color(0xFF757575),
 *     selectedRippleColor = Color(0x3382B1FF)
 * )
 * ```
 */
@Immutable
data class NavColor(
    val backgroundColor: Color = Color(0xFFFFFFFF),        // White
    val borderColor: Color = Color(0xFFE0E0E0),            // Light Grey
    val selectedIconColor: Color = Color(0xFF1A73E8),      // Blue
    val unSelectedIconColor: Color = Color(0xFF9E9E9E),    // Grey 600
    val selectedTextColor: Color = Color(0xFF1A73E8),      // Blue
    val unSelectedTextColor: Color = Color(0xFF9E9E9E),    // Grey 600
    val selectedRippleColor: Color = Color(0x331A73E8)     // 20% Blue Ripple
)
