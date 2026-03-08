package com.android.recipe.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import com.android.recipe.ui.theme.Elevation.extraLarge
import com.android.recipe.ui.theme.Elevation.extraSmall
import com.android.recipe.ui.theme.Elevation.large
import com.android.recipe.ui.theme.Elevation.medium
import com.android.recipe.ui.theme.Elevation.none
import com.android.recipe.ui.theme.Elevation.small

/**
 * Defines a centralized set of elevation values (in DP) used to maintain 
 * consistent depth and shadowing across the application's UI components.
 *
 * @property none 0.dp - No elevation.
 * @property extraSmall 1.dp - Subtle depth for surfaces like cards or sheets.
 * @property small 2.dp - Light elevation.
 * @property medium 4.dp - Standard elevation for buttons or floating elements.
 * @property large 8.dp - Pronounced elevation for prominent components.
 * @property extraLarge 12.dp - Maximum depth for high-priority overlays.
 */
object Elevation {
    val none = 0.dp
    val extraSmall = 1.dp
    val small = 2.dp
    val medium = 4.dp
    val large = 8.dp
    val extraLarge = 12.dp
}

/** Extension property to access elevation values through MaterialTheme */
val MaterialTheme.elevation: Elevation
    get() = Elevation
