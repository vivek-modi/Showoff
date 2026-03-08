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
     * Event triggered when a user selects a specific meal category from the list.
     *
     * @param category The name of the selected meal category.
     */
    data class OnMealClicked(val category: String) : MealsUiEvent
}