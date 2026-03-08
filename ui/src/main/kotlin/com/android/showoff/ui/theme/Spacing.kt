package com.android.showoff.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import com.android.showoff.ui.theme.Spacing.extraLarge
import com.android.showoff.ui.theme.Spacing.extraMedium
import com.android.showoff.ui.theme.Spacing.extraSmall
import com.android.showoff.ui.theme.Spacing.extraXLarge
import com.android.showoff.ui.theme.Spacing.extraXxLarge
import com.android.showoff.ui.theme.Spacing.large
import com.android.showoff.ui.theme.Spacing.medium
import com.android.showoff.ui.theme.Spacing.small

/**
 * A utility object that defines a centralized set of spacing and margin values (in DP)
 * used to maintain visual consistency across the application's UI layout.
 *
 * Use these values for padding, margins, and gaps between components instead of
 * hardcoding pixel or dp values.
 *
 * Access via `MaterialTheme.spacing` for consistent integration with the theme.
 *
 * @property extraSmall 4.dp - Micro spacing for tight component groupings.
 * @property small 8.dp - Small spacing for related elements.
 * @property extraMedium 12.dp - Intermediate spacing for balanced layouts.
 * @property medium 16.dp - Standard spacing for general padding and gutters.
 * @property large 20.dp - Significant spacing to separate distinct UI sections.
 * @property extraLarge 32.dp - Large whitespace for section headers or major breaks.
 * @property extraXLarge 72.dp - Very large spacing for hero sections or prominent offsets.
 * @property extraXxLarge 92.dp - Maximum spacing for specialized layouts.
 */
object Spacing {
    val extraSmall = 4.dp
    val small = 8.dp
    val extraMedium = 12.dp
    val medium = 16.dp
    val large = 20.dp
    val extraLarge = 32.dp
    val extraXLarge = 72.dp
    val extraXxLarge = 92.dp
}

/** Extension property to access spacing values through MaterialTheme */
val MaterialTheme.spacing: Spacing
    get() = Spacing