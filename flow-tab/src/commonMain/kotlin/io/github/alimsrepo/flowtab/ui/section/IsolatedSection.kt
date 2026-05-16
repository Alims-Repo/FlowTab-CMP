package io.github.alimsrepo.flowtab.ui.section

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import io.github.alimsrepo.flowtab.domain.model.NavConfig
import io.github.alimsrepo.flowtab.domain.model.NavItem
import io.github.alimsrepo.flowtab.ui.extension.backgroundBlur
import dev.chrisbanes.haze.HazeState

/**
 * Isolated action button displayed separately from the main navigation bar.
 *
 * Creates a prominent circular button for primary actions. Features:
 * - Circular shape with full height sizing
 * - Optional icon rotation
 * - Background blur support
 * - Border and shadow styling
 * - IME padding support
 *
 * Commonly used for actions like "Add", "Create New", or "Compose".
 *
 * @param modifier Modifier for the isolated button.
 * @param item The isolated navigation item.
 * @param rotation Rotation angle in degrees for the icon.
 * @param config Navigation configuration settings.
 * @param hazeState Optional haze state for blur effects.
 * @param onClick Callback when the button is clicked.
 */
@Composable
internal fun IsolatedSection(
    modifier: Modifier,
    item: NavItem,
    rotation: Float,
    config: NavConfig,
    hazeState: HazeState?,
    onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .imePadding()
            .size(config.height)
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
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier
                .size(config.iconsSize)
                .rotate(rotation),
            imageVector = item.icon,
            tint = config.navColor.unSelectedIconColor,
            contentDescription = item.label
        )
    }
}