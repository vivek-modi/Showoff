package com.android.recipe.ui.components

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
 * A composable function that displays a loading overlay over the given content.
 *
 * The loading overlay consists of a semi-transparent background with a circular progress indicator
 * in the center. It is displayed only when [isLoading] is true. While the overlay is active,
 * it prevents user interaction with the underlying content.
 *
 * @param isLoading A boolean that determines whether the loading overlay is displayed.
 *                  If true, the overlay is shown; otherwise, it's hidden.
 * @param content The composable content that will be displayed behind the loading overlay.
 */
@Composable
fun LoadingOverlay(
    isLoading: Boolean,
    content: @Composable () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        content()
        if (isLoading) {
            LoadingIndicator()
        }
    }
}

/**
 * A private composable function that displays the loading indicator.
 *
 * It creates a semi-transparent background and a circular progress indicator in the center.
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