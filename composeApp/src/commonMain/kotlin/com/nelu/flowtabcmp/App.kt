package com.nelu.flowtabcmp

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import io.github.alimsrepo.flowtab.BottomNavigation
import io.github.alimsrepo.flowtab.domain.model.NavColor
import io.github.alimsrepo.flowtab.domain.model.NavConfig
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var text by remember { mutableStateOf("") }

        val bottomNavController = rememberNavController()
        val routeId = bottomNavController.currentBackStackEntryAsState().value?.destination?.route

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .imePadding(),
        ) {
            val hazeState = rememberHazeState()

            NavHost(
                modifier = Modifier
                    .hazeSource(state = hazeState)
                    .background(
                        color = MaterialTheme.colorScheme.background
                    ).fillMaxSize(),
                navController = bottomNavController,
                startDestination = BottomNavigationItems.Home.navItem.id
            ) {
                BottomNavigationItems.items.forEach { item ->
                    navScreen(item) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            when (item) {
                                BottomNavigationItems.Home -> Text("Home")
                                BottomNavigationItems.Profile -> Text("Profile")
                                BottomNavigationItems.Search -> Text(text)
                                BottomNavigationItems.Settings -> Text("Settings")
                            }
                        }
                    }
                }
            }

            val navColor = NavColor(
                backgroundColor = MaterialTheme.colorScheme.background,
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                borderColor = MaterialTheme.colorScheme.outline.copy(0.1F),
                unSelectedIconColor = MaterialTheme.colorScheme.outline,
                unSelectedTextColor = MaterialTheme.colorScheme.outline,
                selectedRippleColor = Color(0xFF000000).copy(alpha = 0.1f)
            )

            BottomNavigation(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 8.dp),
                config = NavConfig(
                    navColor = navColor,
                    blurIntensity = 0.5F
                ),
                items = BottomNavigationItems.Companion.items.map { it.navItem },
                hazeState = hazeState,
                selectedId = routeId ?: BottomNavigationItems.Home.navItem.id,
                onItemSelected = { item ->
                    if (item == BottomNavigationItems.Search)
                        bottomNavController.navigate(item.id)
                    else
                        bottomNavController.navigate(item.id) {
                            launchSingleTop = true
                            restoreState = true

                            popUpTo(bottomNavController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                },
                onQueryChange = {
                    text = it
                },
                onSearch = {
                    text = "Searching"
                }
            )
        }
    }
}

fun NavGraphBuilder.navScreen(
    navItem: BottomNavigationItems,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        content = content,
        route = navItem.navItem.id,
        enterTransition = { fadeIn(animationSpec = tween(200)) },
        exitTransition = { fadeOut(animationSpec = tween(200)) },
        popEnterTransition = { fadeIn(animationSpec = tween(200)) },
        popExitTransition = { fadeOut(animationSpec = tween(200)) }
    )
}