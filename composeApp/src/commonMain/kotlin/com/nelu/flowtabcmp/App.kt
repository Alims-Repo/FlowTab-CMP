package com.nelu.flowtabcmp

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import io.github.alimsrepo.flowtab.BottomNavigation
import io.github.alimsrepo.flowtab.domain.model.BadgeData
import io.github.alimsrepo.flowtab.domain.model.NavColor
import io.github.alimsrepo.flowtab.domain.model.NavConfig
import io.github.alimsrepo.flowtab.domain.model.NavIndicator
import io.github.alimsrepo.flowtab.domain.model.NavItem
import io.github.alimsrepo.flowtab.domain.model.NavItemType
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    var rotation by remember { mutableStateOf(0f) }
    val animatedRotation by animateFloatAsState(rotation)
    val hazeState = rememberHazeState()

    val navItems1 = remember {
        listOf(
            NavItem(id = "home", label = "Home", icon = Icons.Outlined.Home, selectedIcon = Icons.Filled.Home),
            NavItem(id = "search", label = "Search", icon = Icons.Default.Search, type = NavItemType.Search),
            NavItem(id = "favorites", label = "Favorites", icon = Icons.Outlined.Favorite, selectedIcon = Icons.Filled.Favorite, badge = BadgeData(count = 5)),
            NavItem(id = "profile", label = "Profile", icon = Icons.Outlined.Person, selectedIcon = Icons.Filled.Person, badge = BadgeData(showDot = true))
        )
    }

    val navItems2 = remember {
        listOf(
            NavItem(id = "home", label = "Home", icon = Icons.Outlined.Home, selectedIcon = Icons.Filled.Home),
            NavItem(id = "search", label = "Search", icon = Icons.Outlined.Search, selectedIcon = Icons.Filled.Search),
            NavItem(id = "favorites", label = "Favorites", icon = Icons.Outlined.Favorite, selectedIcon = Icons.Filled.Favorite, badge = BadgeData(count = 5)),
            NavItem(id = "profile", label = "Profile", icon = Icons.Outlined.Person, selectedIcon = Icons.Filled.Person, badge = BadgeData(showDot = true))
        )
    }

    val navItems3 = remember(animatedRotation) {
        listOf(
            NavItem(id = "home", label = "Home", icon = Icons.Outlined.Home, selectedIcon = Icons.Filled.Home),
            NavItem(id = "favorites", label = "Favorites", icon = Icons.Outlined.Favorite, selectedIcon = Icons.Filled.Favorite, badge = BadgeData(count = 5)),
            NavItem(id = "profile", label = "Profile", icon = Icons.Outlined.Person, selectedIcon = Icons.Filled.Person, badge = BadgeData(showDot = true)),
            NavItem(id = "add", label = "Add", icon = Icons.Outlined.Add, type = NavItemType.Isolated(animatedRotation))
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        var selectedId1 by remember { mutableStateOf("home") }
        BottomNavigation(
            items = navItems1,
            modifier = Modifier,
            selectedId = selectedId1,
            onItemSelected = { item -> selectedId1 = item.id },
            config = FlowTabStyle.MODERN_PILL.config,
            hazeState = hazeState,
            onQueryChange = { query ->
                // Handle search query
            },
            onSearch = { query ->
                // Handle search submit
            },
            contentPadding = PaddingValues(24.dp)
        )

        HorizontalDivider()

        var selectedId3 by remember { mutableStateOf("home") }
        BottomNavigation(
            items = navItems3,
            modifier = Modifier,
            selectedId = selectedId3,
            onItemSelected = { item ->
                if (item.id == "add") {
                    rotation = if (rotation == 45F) 0f else 45F
                } else {
                    if (rotation == 45F) rotation = 0f
                    selectedId3 = item.id
                }


//                rotation = if (item.id == "add") 45F else {
//                    selectedId3 = item.id
//                    0f
//                }
            },
            config = FlowTabStyle.MODERN_PILL2.config,
            hazeState = hazeState,
            onQueryChange = { query ->
                // Handle search query
            },
            onSearch = { query ->
                // Handle search submit
            },
            contentPadding = PaddingValues(24.dp)
        )

        HorizontalDivider()

        var selectedId2 by remember { mutableStateOf("home") }
        BottomNavigation(
            items = navItems2,
            modifier = Modifier,
            selectedId = selectedId2,
            onItemSelected = { item -> selectedId2 = item.id },
            config = FlowTabStyle.INSTAGRAM.config,
            hazeState = hazeState,
            onQueryChange = { query ->
                // Handle search query
            },
            onSearch = { query ->
                // Handle search submit
            },
            contentPadding = PaddingValues(0.dp, vertical = 24.dp)
        )

        HorizontalDivider()

        var selectedId4 by remember { mutableStateOf("home") }
        BottomNavigation(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            items = navItems2,
            selectedId = selectedId4,
            onItemSelected = { item -> selectedId4 = item.id },
            config = FlowTabStyle.FLOATING_MINIMAL.config,
            hazeState = hazeState,
            onQueryChange = { query ->
                // Handle search query
            },
            onSearch = { query ->
                // Handle search submit
            },
            contentPadding = PaddingValues(0.dp, vertical = 24.dp)
        )

        HorizontalDivider()
    }
}

// ============================================================================
// STYLE DEFINITIONS
// ============================================================================

enum class FlowTabStyle(
    val displayName: String,
    val description: String,
    val config: NavConfig,
) {
    // 1. Modern Pill Style (Default)
    MODERN_PILL(
        displayName = "Modern Pill",
        description = "Rounded pill design with glassmorphism, perfect for modern apps",
        config = NavConfig(
            height = 60.dp,
            cornerRadius = 60.dp,
            maxWidth = 460.dp,
            animationDuration = 250,
            enableBlur = true,
            blurIntensity = 0.95f,
            showLabels = true,
            hideLabelsOnSearchExpand = true,
            showBorder = true,
            elevation = 0.dp,
            navColor = NavColor(
                backgroundColor = Color(0xFFFFFFFF).copy(alpha = 0.9f),
                borderColor = Color(0xFFE0E0E0),
                selectedIconColor = Color(0xFF6200EE),
                unSelectedIconColor = Color(0xFF666666),
                selectedTextColor = Color(0xFF6200EE),
                unSelectedTextColor = Color(0xFF999999),
                selectedRippleColor = Color(0x336200EE),
            ),
            navIndicator = NavIndicator.Ripple(
                indicatorPadding = 4.dp,
            )
        )
    ),

    MODERN_PILL2(
        displayName = "Modern Pill",
        description = "Rounded pill design with glassmorphism, perfect for modern apps",
        config = NavConfig(
            height = 52.dp,
            cornerRadius = 26.dp,
            maxWidth = 460.dp,
            animationDuration = 250,
            enableBlur = true,
            blurIntensity = 0.95f,
            showLabels = false,
            hideLabelsOnSearchExpand = true,
            showBorder = true,
            elevation = 0.dp,
            navColor = NavColor(
                backgroundColor = Color(0xFFFFFFFF).copy(alpha = 0.9f),
                borderColor = Color(0xFFE0E0E0),
                selectedIconColor = Color(0xFF6200EE),
                unSelectedIconColor = Color(0xFF666666),
                selectedTextColor = Color(0xFF6200EE),
                unSelectedTextColor = Color(0xFF999999),
                selectedRippleColor = Color(0x336200EE),
            ),
            navIndicator = NavIndicator.Ripple(
                indicatorPadding = 4.dp,
            )
        )
    ),

    // 2. Instagram Style
    INSTAGRAM(
        displayName = "Instagram",
        description = "Flat design with no rounded corners, minimal and clean",
        config = NavConfig(
            height = 60.dp,
            cornerRadius = 0.dp,
            maxWidth = 600.dp,
            animationDuration = 200,
            enableBlur = false,
            showLabels = false,
            iconsSize = 32.dp,
            showBorder = false,
            elevation = 0.dp,
            navColor = NavColor(
                backgroundColor = Color.Black,
                selectedIconColor = Color.White,
                unSelectedIconColor = Color.Gray,
                selectedRippleColor = Color(0x00000000)
            ),
            navIndicator = NavIndicator.Line(2.dp, 32.dp)
        )
    ),

    // 3. Floating Minimal
    FLOATING_MINIMAL(
        displayName = "Floating Minimal",
        description = "Compact floating bar, ideal for one-handed use",
        config = NavConfig(
            height = 56.dp,
            cornerRadius = 28.dp,
            maxWidth = 320.dp,
            animationDuration = 300,
            enableBlur = false,
            showLabels = false,
            showBorder = false,
            elevation = 12.dp,
            navColor = NavColor(
                backgroundColor = Color(0xFF1E1E1E),
                selectedIconColor = Color(0xFF00D9FF),
                unSelectedIconColor = Color(0xFF666666),
                selectedRippleColor = Color(0x3300D9FF)
            ),
            navIndicator = NavIndicator.Dot(
                size = 8.dp,
                indicatorPadding = 6.dp,
            )
        )
    )
}