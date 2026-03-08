package com.android.showoff.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import com.android.showoff.ui.theme.Size.Card
import com.android.showoff.ui.theme.Size.SmallCard
import com.android.showoff.ui.theme.Size.extraSmall
import com.android.showoff.ui.theme.Size.large
import com.android.showoff.ui.theme.Size.medium
import com.android.showoff.ui.theme.Size.small

/**
 * Defines the standard dimensions and spacing values used throughout the application's UI.
 *
 * This object provides a centralized source for dp values to ensure visual consistency
 * across paddings, margins, and specific component sizes like cards.
 *
 * @property extraSmall standard 4.dp spacing.
 * @property small standard 8.dp spacing.
 * @property medium standard 16.dp spacing.
 * @property large standard 24.dp spacing.
 * @property SmallCard standard size for miniature card components.
 * @property Card standard size for default card components.
 */
object Size {
    val extraSmall = 4.dp
    val small = 8.dp
    val medium = 16.dp
    val large = 24.dp
    val SmallCard = 64.dp
    val Card = 132.dp
}

/**
 * Accessor for the custom [Size] tokens within the [MaterialTheme].
 *
 * Provides a consistent way to access application-specific dimensions and spacing
 * alongside standard Material Design tokens.
 */
val MaterialTheme.size: Size
    get() = Size