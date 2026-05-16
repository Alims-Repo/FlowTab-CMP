package io.github.alimsrepo.flowtab.domain.model

import androidx.compose.runtime.Immutable

/**
 * Determines the layout and interaction behaviour of a [NavItem].
 *
 * Pass one of the three subtypes as [NavItem.type]:
 * - [Standard] — a regular destination in the main bar (default).
 * - [Search] — expands the item into a full-width search field.
 * - [Isolated] — renders a separate circular action button to the right of the bar.
 *
 * @see NavItem
 */
@Immutable
sealed class NavItemType {
    /**
     * A regular navigation destination displayed inside the main bar.
     *
     * This is the default type; no [NavItem.type] argument is required for standard items.
     */
    data object Standard : NavItemType()

    /**
     * Transforms the item into an expandable search field.
     *
     * When the user taps the search button it animates into a full-width text input.
     * Tapping any other item or pressing the dismiss button collapses it and returns
     * navigation to the previously selected destination.
     *
     * At most one [Search] item should be present in the list.
     */
    data object Search : NavItemType()

    /**
     * Renders the item as a separate circular action button to the right of the main bar.
     *
     * Useful for prominent primary actions such as "Compose" or "Create".
     *
     * @property rotation Icon rotation in degrees. Useful for turning a standard plus icon
     *   into an X or a different visual form. Default: `0f` (no rotation).
     */
    data class Isolated(val rotation: Float = 0f) : NavItemType()
}
