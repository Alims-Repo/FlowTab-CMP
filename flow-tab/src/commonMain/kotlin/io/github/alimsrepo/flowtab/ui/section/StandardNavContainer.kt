package io.github.alimsrepo.flowtab.ui.section

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import io.github.alimsrepo.flowtab.domain.model.NavConfig
import io.github.alimsrepo.flowtab.domain.model.NavItem
import io.github.alimsrepo.flowtab.ui.component.navitem.NavItemView
import io.github.alimsrepo.flowtab.ui.extension.backgroundBlur
import io.github.alimsrepo.flowtab.util.math.interpolate
import dev.chrisbanes.haze.HazeState
import io.github.alimsrepo.flowtab.domain.model.NavIndicator

/**
 * Container for standard navigation items with selection indicator.
 *
 * Displays navigation items in a horizontal row with:
 * - Animated selection indicator that slides between items
 * - Background blur effect (if enabled)
 * - Border and shadow styling
 * - Responsive width distribution across items
 * - Label visibility control based on search state
 *
 * @param modifier Modifier for the container.
 * @param items List of standard navigation items to display.
 * @param selectedItem The currently selected item.
 * @param isSearchExpanded Whether search is currently expanded.
 * @param animatedHeight Animated height value synced with search expansion.
 * @param config Navigation configuration settings.
 * @param hazeState Optional haze state for blur effects.
 * @param density Current density for unit conversions.
 * @param onItemClick Callback for item clicks.
 * @param onSizeChanged Callback when container size changes.
 */
@Composable
internal fun StandardNavContainer(
    modifier: Modifier,
    items: List<NavItem>,
    selectedItem: NavItem,
    isSearchExpanded: Boolean,
    animatedHeight: Dp,
    config: NavConfig,
    hazeState: HazeState?,
    density: Density,
    onItemClick: (NavItem) -> Unit
) {
    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    val shape = RoundedCornerShape(config.cornerRadius)
//    val borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(animatedHeight)
                .onSizeChanged { size ->
                    containerSize = size
                }
                .shadow(elevation = config.elevation, shape = shape)
                .clip(shape)
                .background(
                    color = config.navColor.backgroundColor.copy(
                        if (config.enableBlur && hazeState != null) 1f else 0.5f
                    )
                )
                .backgroundBlur(config, hazeState)
                .background(
                    color = config.navColor.backgroundColor.copy(config.blurIntensity)
                )
                .then(
                    if (config.showBorder) {
                        Modifier.border(1.dp, config.navColor.borderColor, shape)
                    } else Modifier
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val containerWidthDp = with(density) { containerSize.width.toDp() }
            val showLabels = config.showLabels && (!isSearchExpanded || !config.hideLabelsOnSearchExpand)

            items.forEach { item ->
                val mappedWidth = interpolate(
                    value = animatedHeight,
                    inputMin = 48.dp,
                    inputMax = config.height,
                    outputMin = 0.dp,
                    outputMax = if (containerWidthDp > 0.dp) containerWidthDp / items.size else 0.dp
                )

                Box(
                    modifier = if (item == selectedItem && isSearchExpanded) {
                        Modifier.weight(1f)
                    } else {
                        Modifier.width(mappedWidth)
                    }
                ) {
                    NavItemView(
                        item = item,
                        config = config,
                        isSelected = selectedItem == item,
                        showLabel = showLabels,
                        onClick = { onItemClick(item) }
                    )
                }
            }
        }

        if (!isSearchExpanded && containerSize.width > 0) {
            val currentIndex = items.indexOfFirst { it == selectedItem }.coerceAtLeast(0)
            val itemWidth = with(density) {
                if (containerSize.width > 0 && items.isNotEmpty()) {
                    (containerSize.width / items.size).toDp()
                } else 0.dp
            }

            val indicatorOffset by animateDpAsState(
                targetValue = if (itemWidth > 0.dp) {
                    (itemWidth * currentIndex) + config.navIndicator.indicatorPadding
                } else 0.dp,
                animationSpec = tween(config.animationDuration),
                label = "indicatorOffset"
            )

            val indicatorWidth = (itemWidth - config.navIndicator.indicatorPadding * 2).coerceAtLeast(0.dp)

            // Resolve indicator color: Color.Unspecified (the default) falls back to
            // the appropriate NavColor value so the bar "just works" out of the box.
            val indicatorColor = when (val ind = config.navIndicator) {
                is NavIndicator.Ripple -> if (ind.color == Color.Unspecified) config.navColor.selectedRippleColor else ind.color
                is NavIndicator.Dot    -> if (ind.color == Color.Unspecified) config.navColor.selectedIconColor  else ind.color
                is NavIndicator.Line   -> if (ind.color == Color.Unspecified) config.navColor.selectedIconColor  else ind.color
            }

            if (indicatorWidth > 0.dp) {
                when(config.navIndicator) {
                    is NavIndicator.Dot -> {
                        Box(
                            modifier = Modifier
                                .offset(x = (indicatorOffset + ((indicatorWidth - config.navIndicator.size) / 2)))
                                .offset(y = animatedHeight - config.navIndicator.size - config.navIndicator.indicatorPadding)
                                .size(config.navIndicator.size)
                                .background(
                                    color = indicatorColor,
                                    shape = CircleShape
                                )
                        )
                    }
                    is NavIndicator.Line -> {
                        Box(
                            modifier = Modifier
                                .offset(x = (indicatorOffset + ((indicatorWidth - config.navIndicator.width) / 2)))
                                .offset(y = animatedHeight - config.navIndicator.height - config.navIndicator.indicatorPadding)
                                .height(config.navIndicator.height)
                                .width(config.navIndicator.width)
                                .background(
                                    color = indicatorColor,
                                    shape = CircleShape
                                )
                        )
                    }

                    is NavIndicator.Ripple -> {
                        Box(
                            modifier = Modifier
                                .offset(x = indicatorOffset)
                                .width(indicatorWidth)
                                .height(animatedHeight)
                                .padding(vertical = config.navIndicator.indicatorPadding)
                                .background(
                                    color = indicatorColor,
                                    shape = RoundedCornerShape(52.dp)
                                )
                        )
                    }
                }
            }
        }
    }
}