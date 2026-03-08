package com.android.recipe.domain.model

/**
 * Represents the comprehensive details of a specific food recipe.
 *
 * @property id The unique identifier for the recipe.
 * @property name The name or title of the dish.
 * @property category The food category the recipe belongs to (e.g., Seafood, Dessert).
 * @property area The culinary region or origin of the recipe (e.g., Italian, Moroccan).
 * @property instructions The step-by-step cooking directions.
 * @property imageUrl The URL for the thumbnail or main image of the dish.
 * @property youtubeUrl The optional URL for a video tutorial on YouTube.
 * @property sourceUrl The optional original web link where the recipe was found.
 * @property ingredients A list of [IngredientItem] required to prepare the recipe.
 */
data class RecipeDetails(
    val id: String,
    val name: String,
    val category: String,
    val area: String,
    val instructions: String,
    val imageUrl: String,
    val youtubeUrl: String?,
    val sourceUrl: String?,
    val ingredients: List<IngredientItem>,
)
