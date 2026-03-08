package com.android.recipe.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A reusable application scaffold that provides the standard screen structure.
 *
 * This composable wraps [Scaffold] from Material3 and centralizes common layout elements
 * used across screens such as the top bar and snackbar host. It helps maintain a
 * consistent layout while allowing individual screens to provide their own content.
 *
 * @param modifier Optional [Modifier] applied to the underlying [Scaffold].
 * @param snackbarHost Slot for displaying snackbars. By default, no snackbar host is provided.
 * @param topBar Slot for displaying a top app bar or other header UI.
 * @param content Main screen content. The provided [PaddingValues] should be applied to the
 * content layout to ensure it is properly offset from system bars and scaffold elements
 * such as the top bar.
 */
@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    snackbarHost: @Composable () -> Unit = {},
    topBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        snackbarHost = snackbarHost,
        content = content,
    )
}