package com.android.showoff.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A top-level scaffold component that provides the standard layout structure for the application.
 *
 * @param modifier The [Modifier] to be applied to the scaffold.
 * @param content The main content UI of the screen, which receives [PaddingValues] to ensure
 * content is properly offset from any system bars or persistent UI elements.
 */
@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = snackbarHost,
        content = content,
    )
}