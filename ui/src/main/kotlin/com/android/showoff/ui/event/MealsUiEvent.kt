package com.android.showoff.ui.event

/**
 * Represents the various user interface events or interactions that can occur
 * within the Meals screen.
 */
sealed interface MealsUiEvent {
    /**
     * Event triggered when the user requests to refresh the list of meals,
     * typically via a swipe-to-refresh gesture or a refresh button.
     */
    data object Refresh : MealsUiEvent

    /**
     * Event triggered when a user selects a specific meal from the list.
     *
     * @param mealId The unique identifier of the clicked meal.
     */
    data class OnMealClicked(val mealId: String) : MealsUiEvent
}