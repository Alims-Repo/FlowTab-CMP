package io.github.alimsrepo.flowtab.ui.container

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import io.github.alimsrepo.flowtab.domain.model.NavConfig
import io.github.alimsrepo.flowtab.domain.model.NavItem
import io.github.alimsrepo.flowtab.domain.model.NavItemType
import io.github.alimsrepo.flowtab.ui.section.IsolatedSection
import io.github.alimsrepo.flowtab.ui.section.SearchSection
import io.github.alimsrepo.flowtab.ui.section.StandardNavContainer
import dev.chrisbanes.haze.HazeState

/**
 * Main content container for the bottom navigation layout.
 *
 * Orchestrates the layout and animations of all navigation sections:
 * - Standard navigation items container
 * - Optional search section
 * - Optional isolated action button
 *
 * Handles width calculations and responsive sizing based on available space
 * and search expansion state.
 *
 * @param standardItems List of standard navigation items to display.
 * @param searchItem Optional search-type navigation item.
 * @param isolatedItem Optional isolated-type navigation item.
 * @param selectedItem The currently selected navigation item.
 * @param searchQuery Current search query text.
 * @param isSearchExpanded Whether the search bar is expanded.
 * @param config Navigation configuration settings.
 * @param hazeState Optional haze state for blur effects.
 * @param density Current density for unit conversions.
 * @param onItemClick Callback for navigation item clicks.
 * @param onSearchQueryChange Callback for search query changes.
 * @param onSearch Callback when search is submitted.
 * @param onSearchExpand Callback to expand the search bar.
 * @param onSearchCollapse Callback to collapse the search bar.
 */
@Composable
internal fun BoxScope.BottomNavContent(
    standardItems: List<NavItem>,
    searchItem: NavItem?,
    isolatedItem: NavItem?,
    selectedItem: NavItem,
    searchQuery: String,
    isSearchExpanded: Boolean,
    config: NavConfig,
    hazeState: HazeState?,
    density: Density,
    onItemClick: (NavItem) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onSearchExpand: () -> Unit,
    onSearchCollapse: () -> Unit
) {
    var containerWidth by remember { mutableIntStateOf(0) }

    val containerWidthDp = remember(containerWidth) {
        with(density) {
            val width = containerWidth.toDp()
            if (width > config.maxWidth) config.maxWidth else width
        }
    }

    val searchWidth by animateDpAsState(
        targetValue = if (isSearchExpanded) containerWidthDp - config.height else config.height,
        animationSpec = tween(config.animationDuration),
        label = "searchWidth"
    )

    val searchHeight by animateDpAsState(
        targetValue = if (isSearchExpanded) 48.dp else config.height,
        animationSpec = tween(config.animationDuration),
        label = "searchHeight"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .onSizeChanged { size -> containerWidth = size.width }
    ) {
        if (standardItems.isNotEmpty()) {
            StandardNavContainer(
                modifier = Modifier
                    .width(
                        when {
                            searchItem != null && isolatedItem != null ->
                                containerWidthDp - searchWidth - config.height - 20.dp
                            searchItem != null || isolatedItem != null ->
                                containerWidthDp - searchWidth - 12.dp
                            else -> containerWidthDp
                        }
                    ).align(
                        alignment = if (searchItem == null && isolatedItem == null)
                            Alignment.BottomCenter else Alignment.BottomStart
                    ),
                items = standardItems,
                selectedItem = selectedItem,
                isSearchExpanded = isSearchExpanded,
                animatedHeight = searchHeight,
                config = config,
                hazeState = hazeState,
                density = density,
                onItemClick = { item ->
                    if (isSearchExpanded) {
                        onSearchCollapse()
                    } else {
                        onItemClick(item)
                    }

                }
            )
        }

        searchItem?.let { item ->
            SearchSection(
                modifier = Modifier.align(Alignment.BottomEnd),
                item = item,
                searchQuery = searchQuery,
                isExpanded = isSearchExpanded,
                width = searchWidth,
                height = searchHeight,
                config = config,
                hazeState = hazeState,
                onExpand = onSearchExpand,
                onQueryChange = onSearchQueryChange,
                onCollapse = onSearchCollapse,
                onSearch = onSearch
            )
        }

        isolatedItem?.let { item ->
            val rotation = (item.type as? NavItemType.Isolated)?.rotation ?: 0f
            IsolatedSection(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .then(
                        // When both search and isolated coexist, push isolated left of the search bubble
                        if (searchItem != null) Modifier.padding(end = searchWidth + 8.dp) else Modifier
                    ),
                item = item,
                rotation = rotation,
                config = config,
                hazeState = hazeState,
                onClick = { onItemClick(item) }
            )
        }
    }
}