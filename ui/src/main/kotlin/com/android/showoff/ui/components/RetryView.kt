package com.android.showoff.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.android.showoff.ui.R
import com.android.showoff.ui.theme.spacing

/**
 * A composable component that displays an error message and a retry button.
 * This is typically used to handle failure states in the UI, allowing users
 * to attempt the operation again.
 *
 * @param modifier The [Modifier] to be applied to this view.
 * @param onRetryClick The callback to be invoked when the retry button is clicked.
 */
@Composable
fun RetryView(
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_retry),
            alignment = Alignment.Center,
            contentDescription = null,
        )
        Text(
            text = stringResource(R.string.error_generic_retry),
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
            textAlign = TextAlign.Center,
        )
        Button(
            onClick = onRetryClick,
            modifier = Modifier.padding(vertical = MaterialTheme.spacing.large),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            )
        ) {
            Text(
                text = stringResource(R.string.retry_label),
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}