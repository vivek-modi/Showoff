package com.android.showoff.ui.state

import com.android.showoff.domain.model.RecipeDetails

/**
 * Represents the UI state for the meal details screen.
 *
 * @property isLoading Indicates whether the meal details are currently being loaded.
 * @property details The detailed information about the recipe, or null if not yet loaded.
 * @property error An error message if the loading process fails, otherwise null.
 */
data class MealDetailsUiState(
    val isLoading: Boolean = true,
    val details: RecipeDetails? = null,
    val error: String? = null,
)