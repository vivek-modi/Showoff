package com.android.showoff.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Defines the navigation structure and routes for the meals-related features of the application.
 *
 * This class typically handles the destinations, navigation arguments, and deep links
 * required to navigate between different screens in the meal category or detail flows.
 */
@Serializable
data class MealsNavigation(val category: String) : NavKey