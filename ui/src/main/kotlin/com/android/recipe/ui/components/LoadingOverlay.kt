package com.android.recipe.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput

/**
 * A composable function that displays a loading overlay over the given content with an expressive transition.
 *
 * It uses [AnimatedContent] to provide a Material 3 expressive transition
 * between the loading state and the content, including scaling and fading.
 *
 * @param isLoading A boolean that determines whether the loading overlay is displayed.
 * @param content The composable content that will be displayed when not loading.
 */
@Composable
fun LoadingOverlay(
    isLoading: Boolean,
    content: @Composable () -> Unit,
) {
    AnimatedContent(
        targetState = isLoading,
        transitionSpec = {
            if (targetState) {
                fadeIn() togetherWith fadeOut()
            } else {
                (fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) +
                        scaleIn(
                            initialScale = 0.92f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow,
                            )
                        )) togetherWith
                        fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow))
            }
        },
        label = "LoadingOverlayTransition"
    ) { loading ->
        if (loading) {
            LoadingIndicator()
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                content()
            }
        }
    }
}

/**
 * A private composable function that displays the loading indicator.
 */
@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = { }
            )
            .pointerInput(Unit) {},
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}