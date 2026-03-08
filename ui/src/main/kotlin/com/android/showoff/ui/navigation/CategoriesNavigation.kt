package com.android.showoff.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Navigation key representing the categories screen destination.
 *
 * Used with the Navigation3 library to define the route for displaying
 * the list of available content categories.
 */
@Serializable
data object CategoriesNavigation : NavKey