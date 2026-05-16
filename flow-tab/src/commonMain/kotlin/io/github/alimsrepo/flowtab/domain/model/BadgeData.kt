package io.github.alimsrepo.flowtab.domain.model

import androidx.compose.runtime.Immutable

/**
 * Badge configuration for a [NavItem].
 *
 * Controls whether a badge is visible and what it shows. A badge is only rendered when
 * [showDot] is `true` or [count] is a positive integer — passing `count = 0` produces
 * no visible badge, making it safe to bind directly to an unread-count value.
 *
 * @property count Numeric value to display inside the badge. Values above 99 are shown as
 *   `"99+"`. A value of `null` or `0` does not render a badge circle.
 * @property showDot When `true`, renders a small dot regardless of [count]. Use this to
 *   signal the presence of new content without exposing an exact number.
 *
 * @see NavItem.badge
 *
 * Example:
 * ```kotlin
 * NavItem(
 *     id = "inbox",
 *     label = "Inbox",
 *     icon = Icons.Outlined.Mail,
 *     selectedIcon = Icons.Filled.Mail,
 *     badge = BadgeData(count = unreadCount) // hidden automatically when unreadCount == 0
 * )
 *
 * // Dot-only badge (e.g. "something new" without a count)
 * BadgeData(showDot = true)
 * ```
 */
@Immutable
data class BadgeData(
    val count: Int? = null,
    val showDot: Boolean = false
)
