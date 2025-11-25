package com.nelu.flowtabcmp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import io.github.alimsrepo.flowtab.domain.model.NavItem
import io.github.alimsrepo.flowtab.domain.model.NavItemType

sealed class BottomNavigationItems {

    abstract val navItem: NavItem

    data object Home : BottomNavigationItems() {
        override val navItem: NavItem
            get() = NavItem(
                id = "home",
                label = "Home",
                icon = Icons.Outlined.Home,
                selectedIcon = Icons.Filled.Home
            )
    }

    data object Profile : BottomNavigationItems() {
        override val navItem: NavItem
            get() = NavItem(
                id = "profile",
                label = "Profile",
                icon = Icons.Outlined.Person,
                selectedIcon = Icons.Filled.Person
            )
    }

    data object Settings : BottomNavigationItems() {
        override val navItem: NavItem
            get() = NavItem(
                id = "settings",
                label = "Settings",
                icon = Icons.Outlined.Settings,
                selectedIcon = Icons.Filled.Settings
            )
    }

    data object Search : BottomNavigationItems() {
        override val navItem: NavItem
            get() = NavItem(
                id = "search",
                label = "Search",
                icon = Icons.Default.Search,
                type = NavItemType.Search
            )
    }

    companion object Companion {
        val items by lazy { listOf(Home, Profile, Settings, Search) }
    }
}