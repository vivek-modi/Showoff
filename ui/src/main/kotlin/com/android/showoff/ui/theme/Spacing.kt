package com.android.showoff.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Defines standard spacing values used across the app */
object Spacing {
    val extraSmall = 4.dp
    val small = 8.dp
    val extraMedium = 12.dp
    val medium = 16.dp
    val large = 20.dp
    val extraLarge = 32.dp
}

/** Extension property to access spacing values through MaterialTheme */
val MaterialTheme.spacing: Spacing
    get() = Spacing