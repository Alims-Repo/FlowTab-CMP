package io.github.alimsrepo.flowtab.ui.extension

import androidx.compose.ui.Modifier
import io.github.alimsrepo.flowtab.domain.model.NavConfig
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect

/**
 * Applies background blur effect to a composable based on configuration.
 *
 * Uses the Haze library to create a progressive vertical gradient blur effect.
 * The blur is only applied when [NavConfig.enableBlur] is true and a valid
 * [HazeState] is provided.
 *
 * @param config Navigation configuration containing blur settings.
 * @param hazeState Optional haze state for managing blur effects. If null,
 *                  no blur is applied regardless of config.
 * @return Modified [Modifier] with blur effect applied, or unchanged [Modifier]
 *         if blur is disabled or state is null.
 */
fun Modifier.backgroundBlur(config: NavConfig, hazeState: HazeState?) : Modifier {
    return if (config.enableBlur && hazeState != null) {
        hazeEffect(state = hazeState) {
            progressive = HazeProgressive.verticalGradient(
                startIntensity = config.blurIntensity,
                endIntensity = config.blurIntensity,
                preferPerformance = true
            )
        }
    } else this
}