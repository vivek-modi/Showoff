package com.android.recipe.ui.event

/**
 * Represents the various user interface events that can occur on the Categories screen.
 *
 * This sealed interface defines the set of actions or interactions initiated by the user
 * or the system that require a state change or side effect within the category-related
 * UI components.
 */
sealed interface CategoriesUiEvent {

    /**
     * Event triggered when the user requests a refresh of the categories list,
     * typically via a swipe-to-refresh gesture or an explicit refresh action.
     */
    data object Refresh : CategoriesUiEvent

    /**
     * Event triggered when the user selects a specific category from the list.
     *
     * @param category The name or identifier of the category that was clicked.
     */
    data class OnCategoryClicked(val category: String) : CategoriesUiEvent
}