package io.github.alimsrepo.flowtab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import io.github.alimsrepo.flowtab.domain.model.NavConfig
import io.github.alimsrepo.flowtab.domain.model.NavItem
import io.github.alimsrepo.flowtab.domain.model.NavItemType
import io.github.alimsrepo.flowtab.domain.model.NavState
import io.github.alimsrepo.flowtab.domain.model.NavStateSaver
import io.github.alimsrepo.flowtab.ui.container.BottomNavContent
import dev.chrisbanes.haze.HazeState

/**
 * A framework-agnostic animated bottom navigation bar for Compose Multiplatform.
 *
 * Manages its own visual and search state internally while delegating every navigation
 * decision to the caller through [onItemSelected]. Works with any navigation library
 * or plain state hoisting — no navigation dependency required.
 *
 * Item types are determined by [NavItem.type]:
 * - [NavItemType.Standard] — regular destination shown in the main bar.
 * - [NavItemType.Search] — expands into a full-width search field; at most one per list.
 * - [NavItemType.Isolated] — a separate circular action button; at most one per list.
 *
 * @param items Non-empty list of navigation items. All [NavItem.id] values must be unique.
 * @param selectedId The ID of the currently selected item, controlled by the caller.
 *   If the value does not match any item (e.g. during a NavHost transition), the first
 *   item is used as a safe fallback instead of crashing.
 * @param onItemSelected Called when the user taps an item or when the search bar collapses.
 *   Update your navigation state and [selectedId] inside this callback.
 * @param modifier Modifier applied to the outermost container.
 * @param config Appearance and behaviour configuration. See [NavConfig].
 * @param hazeState Haze state from your content composable for the background blur effect.
 *   Pass `null` (the default) to disable blur entirely.
 * @param onQueryChange Called on every keystroke inside the expanded search field.
 * @param onSearch Called when the user submits the search query via the IME action.
 * @param contentPadding Padding applied around the navigation bar row.
 *
 * @throws IllegalArgumentException if [items] is empty or contains duplicate IDs.
 *
 * @see NavItem
 * @see NavConfig
 *
 * Example:
 * ```kotlin
 * val hazeState = rememberHazeState()
 *
 * // In your content area
 * Box(modifier = Modifier.hazeSource(hazeState)) { ... }
 *
 * var selectedId by remember { mutableStateOf("home") }
 *
 * BottomNavigation(
 *     items = listOf(
 *         NavItem(id = "home",   label = "Home",   icon = Icons.Outlined.Home,   selectedIcon = Icons.Filled.Home),
 *         NavItem(id = "search", label = "Search", icon = Icons.Default.Search,  type = NavItemType.Search),
 *         NavItem(id = "profile",label = "Profile",icon = Icons.Outlined.Person, selectedIcon = Icons.Filled.Person),
 *     ),
 *     selectedId = selectedId,
 *     hazeState = hazeState,
 *     onItemSelected = { item ->
 *         selectedId = item.id
 *         navController.navigate(item.id)
 *     },
 *     onQueryChange = { query -> viewModel.onSearchQuery(query) }
 * )
 * ```
 */
@Composable
fun BottomNavigation(
    items: List<NavItem>,
    selectedId: String,
    onItemSelected: (NavItem) -> Unit,
    modifier: Modifier = Modifier,
    config: NavConfig = NavConfig(),
    hazeState: HazeState? = null,
    onQueryChange: ((String) -> Unit)? = null,
    onSearch:  ((String) -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
) {
    val density = LocalDensity.current

    require(items.isNotEmpty()) { "Navigation items cannot be empty" }
    require(items.distinctBy { it.id }.size == items.size) { "All items must have unique IDs" }

    // Gracefully coerce an invalid selectedId rather than crashing during recomposition
    // (e.g. when a consumer's NavHost hasn't settled on a valid destination yet)
    val safeSelectedId = if (items.any { it.id == selectedId }) selectedId else items.first().id

    // Separate items by type
    val standardItems = remember(items) { items.filter { it.type is NavItemType.Standard } }
    val searchItem = remember(items) { items.find { it.type is NavItemType.Search } }
    val isolatedItem = remember(items) { items.find { it.type is NavItemType.Isolated } }

    // Internal state for search and animations
    val initialNonSearchId = remember(items) {
        if (searchItem?.id == safeSelectedId) standardItems.firstOrNull()?.id ?: safeSelectedId
        else safeSelectedId
    }
    var state by rememberSaveable(saver = NavStateSaver) {
        mutableStateOf(
            NavState(
                selectedId = safeSelectedId,
                returnToId = initialNonSearchId,
                searchQuery = "",
                isSearchExpanded = searchItem?.id == safeSelectedId
            )
        )
    }

    // Sync with external selectedId (only for non-search items).
    // LaunchedEffect key stays as the raw prop so every external change is observed.
    LaunchedEffect(selectedId) {
        val isSearchScreen = searchItem?.id == safeSelectedId
        if (!isSearchScreen) {
            state = state.copy(selectedId = safeSelectedId)
        }
        state = state.copy(isSearchExpanded = isSearchScreen)
    }

    // Determine selected item for UI
    val selectedItem = remember(safeSelectedId, state.isSearchExpanded, state.returnToId) {
        if (state.isSearchExpanded) {
            items.find { it.id == state.returnToId } ?: standardItems.firstOrNull() ?: items.first()
        } else {
            items.find { it.id == safeSelectedId } ?: items.first()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(contentPadding),
        contentAlignment = Alignment.BottomCenter
    ) {
        BottomNavContent(
            standardItems = standardItems,
            searchItem = searchItem,
            isolatedItem = isolatedItem,
            selectedItem = selectedItem,
            searchQuery = state.searchQuery,
            isSearchExpanded = state.isSearchExpanded,
            config = config,
            hazeState = hazeState,
            density = density,
            onItemClick = { item ->
                onItemSelected(item)
                state = state.copy(selectedId = item.id, returnToId = item.id)
            },
            onSearchQueryChange = { query ->
                state = state.copy(searchQuery = query)
                onQueryChange?.invoke(query)
            },
            onSearchExpand = {
                searchItem?.let { item ->
                    // Capture returnToId synchronously — no LaunchedEffect race
                    state = state.copy(isSearchExpanded = true, returnToId = state.selectedId)
                    onItemSelected(item)
                }
            },
            onSearch = {
                onSearch?.invoke(it)
            },
            onSearchCollapse = {
                val returnItem = items.find { it.id == state.returnToId }
                    ?: standardItems.firstOrNull()
                state = state.copy(isSearchExpanded = false, searchQuery = "")
                returnItem?.let { onItemSelected(it) }
            }
        )
    }
}