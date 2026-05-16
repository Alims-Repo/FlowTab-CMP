package io.github.alimsrepo.flowtab.ui.section

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.alimsrepo.flowtab.domain.model.NavConfig
import io.github.alimsrepo.flowtab.domain.model.NavItem
import io.github.alimsrepo.flowtab.ui.extension.backgroundBlur
import io.github.alimsrepo.flowtab.util.math.interpolate
import dev.chrisbanes.haze.HazeState
import io.github.alimsrepo.flowtab.ui.icons.Close

/**
 * Expandable search section with text input functionality.
 *
 * Provides a search interface that:
 * - Expands from a circular icon to a full search bar
 * - Includes text input with keyboard actions
 * - Shows a close button when keyboard is open
 * - Animates smoothly between collapsed and expanded states
 * - Handles IME (keyboard) padding
 *
 * @param modifier Modifier for the search section.
 * @param item The search navigation item.
 * @param searchQuery Current search query text.
 * @param isExpanded Whether the search bar is expanded.
 * @param width Animated width value for the search bar.
 * @param height Animated height value for the search bar.
 * @param config Navigation configuration settings.
 * @param hazeState Optional haze state for blur effects.
 * @param onExpand Callback to expand the search bar.
 * @param onQueryChange Callback for query text changes.
 * @param onSearch Callback when search is submitted (IME action).
 * @param onCollapse Callback to collapse the search bar.
 */
@Composable
internal fun SearchSection(
    modifier: Modifier,
    item: NavItem,
    searchQuery: String,
    isExpanded: Boolean,
    width: Dp,
    height: Dp,
    config: NavConfig,
    hazeState: HazeState?,
    onExpand: () -> Unit,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onCollapse: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val imeInsets = WindowInsets.ime
    val density = LocalDensity.current

    val isKeyboardOpen by remember {
        derivedStateOf {
            imeInsets.getBottom(density) > 0
        }
    }

    val paddingEnd by animateDpAsState(
        targetValue = if (isKeyboardOpen && isExpanded) config.height else 0.dp,
        animationSpec = tween(160, easing = FastOutSlowInEasing),
        label = "searchPadding"
    )

    val padding = interpolate(
        value = height,
        inputMin = 48.dp,
        inputMax = config.height,
        outputMin = 12.dp,
        outputMax = 16.dp
    )

    Row(
        modifier = modifier
            .padding(end = paddingEnd)
            .width(width)
            .height(height)
            .shadow(elevation = config.elevation, shape = CircleShape)
            .clip(CircleShape)
            .background(config.navColor.backgroundColor)
            .backgroundBlur(config, hazeState)
            .background(
                color = config.navColor.backgroundColor.copy(config.blurIntensity)
            )
            .then(
                if (config.showBorder) {
                    Modifier.border(1.dp, config.navColor.borderColor, CircleShape)
                } else Modifier
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { if (!isExpanded) onExpand() }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(height)
                .padding(padding),
            imageVector = item.icon,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.75f),
            contentDescription = item.label
        )

        if (isExpanded) {
            BasicTextField(
                value = searchQuery,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        onSearch(searchQuery)
                    }
                ),
                decorationBox = { innerTextField ->
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = config.searchPlaceholder,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    innerTextField()
                }
            )
        }
    }

    if (isExpanded && isKeyboardOpen) {
        Box(
            modifier = modifier
                .padding(end = (paddingEnd - config.height).coerceAtLeast(0.dp))
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    color = config.navColor.backgroundColor,
                ).border(
                    width = 1.dp,
                    color = config.navColor.borderColor,
                    shape = CircleShape
                )
                .clickable {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onQueryChange("")
                    onCollapse()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(config.iconsSize),
                imageVector = Close,
                tint = config.navColor.unSelectedIconColor,
                contentDescription = "Close"
            )
        }
    }
}