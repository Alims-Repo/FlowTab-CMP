package io.github.alimsrepo.flowtab.ui.component.badge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.alimsrepo.flowtab.domain.model.BadgeData

/**
 * Displays a badge indicator for navigation items.
 *
 * Shows either a numeric count (with "99+" for values over 99) or a simple dot
 * indicator. The badge is styled with Material 3 error colors for visibility.
 *
 * This is an internal component used by [NavItemView].
 *
 * @param badge The badge data containing count or dot configuration.
 */
@Composable
internal fun Badge(badge: BadgeData) {
    if (badge.showDot || (badge.count != null && badge.count > 0)) {
        Box(
            modifier = Modifier
                .size(if (badge.count != null) 16.dp else 8.dp)
                .background(
                    color = MaterialTheme.colorScheme.error,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            badge.count?.let { count ->
                if (count > 0) {
                    Text(
                        text = if (count > 99) "99+" else count.toString(),
                        color = MaterialTheme.colorScheme.onError,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}