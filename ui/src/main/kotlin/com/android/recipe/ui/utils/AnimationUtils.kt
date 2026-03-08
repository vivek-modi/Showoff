package com.android.recipe.ui.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Material 3 Expressive Easing tokens (approximate)
 */
val M3ExpressiveEasing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)

/**
 * A performance-optimized staggered entrance animation following Material 3 Expressive motion principles.
 * It combines a subtle scale-up, fade-in, and a vertical "slide in" with staggered timing.
 *
 * @param index The position of the item in the list, used to calculate the stagger delay.
 * @param baseDelay A starting delay (in ms) to allow the screen transition or overlay to settle.
 * @param delayStep The amount of delay (in ms) added for each increment of [index].
 */
@Composable
fun Modifier.staggeredEntrance(
    index: Int,
    baseDelay: Int = 120,
    delayStep: Int = 65,
): Modifier {
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.92f) } // M3 expressive starts slightly closer to final scale
    val translationY = remember { Animatable(100f) } // M3 expressive uses shorter, cleaner slides

    LaunchedEffect(Unit) {
        // Staggered delay following M3 motion guidelines, slowed down for a more relaxed feel
        delay(baseDelay + (index * delayStep).toLong())
        
        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 600,
                    easing = M3ExpressiveEasing
                )
            )
        }
        
        launch {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = 0.7f, // M3 expressive clean bounce
                    stiffness = Spring.StiffnessLow // Low stiffness (200f) for a slower, more weighted feel
                )
            )
        }
        
        launch {
            translationY.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = 0.7f,
                    stiffness = Spring.StiffnessLow,
                )
            )
        }
    }

    // Use the lambda-based graphicsLayer to avoid recomposition during animation.
    return this.graphicsLayer {
        this.alpha = alpha.value
        this.scaleX = scale.value
        this.scaleY = scale.value
        this.translationY = translationY.value
        this.clip = true
    }
}

/**
 * A specialized animation for the MealsScreen that creates an expressive "drawing in" effect.
 * It creates a bouncy pop-out scale from the center that overshoots and settles back, 
 * giving a "closing in and out" physical feel.
 *
 * @param index The position of the item in the list.
 * @param baseDelay A starting delay (in ms).
 * @param delayStep The amount of delay (in ms) added for each increment of [index].
 */
@Composable
fun Modifier.centerExpandEntrance(
    index: Int,
    baseDelay: Int = 120,
    delayStep: Int = 60,
): Modifier {
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.7f) }
    val translationY = remember { Animatable(40f) }

    LaunchedEffect(Unit) {
        delay(baseDelay + (index * delayStep).toLong())
        
        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 600,
                    easing = M3ExpressiveEasing
                )
            )
        }
        
        launch {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = 0.55f, // Bouncy pop for expressive overshoot
                    stiffness = 180f // Deliberate, high-end speed
                )
            )
        }

        launch {
            translationY.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = 0.55f,
                    stiffness = 180f
                )
            )
        }
    }

    return this.graphicsLayer {
        this.alpha = alpha.value
        this.scaleX = scale.value
        this.scaleY = scale.value
        this.translationY = translationY.value
    }
}