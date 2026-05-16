package io.github.alimsrepo.flowtab.domain.model

import androidx.compose.runtime.Stable

/**
 * Internal state management for the bottom navigation component.
 *
 * Tracks selection state, search query, and search expansion state.
 * This is an internal implementation detail and not exposed in the public API.
 *
 * @property selectedId The ID of the currently selected navigation item.
 * @property returnToId The ID of the item to navigate back to when search collapses.
 *                      Captured synchronously at the moment search expands, so it
 *                      always reflects the last non-search selected item with no
 *                      async timing dependency.
 * @property searchQuery The current text in the search field.
 * @property isSearchExpanded Whether the search bar is currently expanded.
 */
@Stable
internal data class NavState(
    val selectedId: String,
    val returnToId: String,
    val searchQuery: String = "",
    val isSearchExpanded: Boolean = false
)