package com.android.showoff.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp

object Size {
    val extraSmall = 4.dp
    val small = 8.dp
    val medium = 16.dp
    val large = 24.dp
    val Card = 120.dp
}

val MaterialTheme.size: Size
    get() = Size