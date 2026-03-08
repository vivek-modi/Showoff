package com.android.recipe.ui.event

/**
 * Represents the various user interactions and internal UI events
 * occurring on the Meal Details screen.
 */
sealed interface MealDetailsUiEvent {
    /**
     * Represents a user-initiated request to reload or update the meal details.
     */
    data object Refresh : MealDetailsUiEvent
}