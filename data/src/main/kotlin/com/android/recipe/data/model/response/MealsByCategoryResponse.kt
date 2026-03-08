package com.android.recipe.data.model.response

import kotlinx.serialization.Serializable

/**
 * Data transfer object representing the network response for a list of meals filtered by category.
 *
 * @property meals A nullable list of [MealSummaryResponse] objects containing brief details
 * about each meal. Some API responses may return null when no meals are found.
 */
@Serializable
data class MealsByCategoryResponse(
    val meals: List<MealSummaryResponse>? = null,
)

/**
 * Data transfer object representing a brief summary of a meal returned from the network.
 *
 * @property idMeal The unique identifier for the meal.
 * @property strMeal The name or title of the meal.
 * @property strMealThumb The URL pointing to the meal's thumbnail image.
 */
@Serializable
data class MealSummaryResponse(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
)