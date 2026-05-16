package io.github.alimsrepo.flowtab.util.math

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.times

/**
 * Interpolates a [Dp] value from one range to another.
 *
 * Maps a value from an input range to an output range with linear interpolation.
 * The interpolation progress is clamped between 0 and 1 to ensure the output
 * stays within the defined range.
 *
 * Useful for creating smooth transitions and responsive sizing based on
 * animated or changing values.
 *
 * @param value The current value to interpolate.
 * @param inputMin The minimum value of the input range.
 * @param inputMax The maximum value of the input range.
 * @param outputMin The minimum value of the output range.
 * @param outputMax The maximum value of the output range.
 * @return The interpolated [Dp] value in the output range.
 *
 * Example usage
 * ```kotlin
 * // Map height from 48-60dp range to padding 12-16dp range
 * val padding = interpolate(
 *     value = currentHeight,
 *     inputMin = 48.dp,
 *     inputMax = 60.dp,
 *     outputMin = 12.dp,
 *     outputMax = 16.dp
 * )
 * ```
 */
internal fun interpolate(
    value: Dp,
    inputMin: Dp,
    inputMax: Dp,
    outputMin: Dp,
    outputMax: Dp
): Dp {
    val progress = ((value - inputMin) / (inputMax - inputMin)).coerceIn(0f, 1f)
    return outputMin + progress * (outputMax - outputMin)
}