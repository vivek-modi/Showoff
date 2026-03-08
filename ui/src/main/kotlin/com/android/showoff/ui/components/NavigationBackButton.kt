package com.android.showoff.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.android.showoff.ui.R

/**
 * A standard navigation back button that displays a back arrow icon.
 *
 * @param onClick Callback to be executed when the button is clicked.
 * @param modifier The [Modifier] to be applied to this button.
 */
@Composable
fun NavigationBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_back),
            contentDescription = stringResource(R.string.back_arrow_content_description),
        )
    }
}