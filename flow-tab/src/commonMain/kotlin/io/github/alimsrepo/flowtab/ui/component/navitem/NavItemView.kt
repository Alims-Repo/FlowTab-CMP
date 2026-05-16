package io.github.alimsrepo.flowtab.ui.component.navitem

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.alimsrepo.flowtab.domain.model.NavColor
import io.github.alimsrepo.flowtab.domain.model.NavConfig
import io.github.alimsrepo.flowtab.domain.model.NavItem
import io.github.alimsrepo.flowtab.ui.component.badge.Badge

/**
 * Renders an individual navigation item with icon, label, and optional badge.
 *
 * Handles the visual representation of a single navigation item including:
 * - Icon crossfade animation between selected/unselected states
 * - Label visibility with fade animation
 * - Badge display with proper positioning
 * - Click handling with no ripple effect
 *
 * This is an internal component used within the navigation container.
 *
 * @param item The navigation item to display.
 * @param color The color configuration for icons and text.
 * @param isSelected Whether this item is currently selected.
 * @param showLabel Whether to show the text label.
 * @param onClick Callback invoked when the item is clicked.
 */
@Composable
internal fun NavItemView(
    item: NavItem,
    config: NavConfig,
    isSelected: Boolean,
    showLabel: Boolean,
    onClick: () -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    if (config.enableHaptics) {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                    onClick()
                }
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Crossfade(
                targetState = isSelected,
                animationSpec = tween(200),
                label = "iconCrossfade"
            ) { selected ->
                Icon(
                    modifier = Modifier.size(config.iconsSize),
                    imageVector = if (selected) item.selectedIcon else item.icon,
                    tint = if (isSelected) config.navColor.selectedIconColor else config.navColor.unSelectedIconColor,
                    contentDescription = "${item.label} Icon"
                )
            }

            item.badge?.let { badge ->
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 8.dp, y = (-4).dp)
                ) {
                    Badge(badge = badge)
                }
            }
        }

        AnimatedVisibility(
            visible = showLabel,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.label,
                    style = config.labelStyle.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) config.navColor.selectedTextColor else config.navColor.unSelectedTextColor
                    )
                )
            }
        }
    }
}