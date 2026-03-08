package com.android.showoff.domain.model

/**
 * Represents a category for grouping recipes, such as "Breakfast", "Italian", or "Vegan".
 *
 * @property id The unique identifier for the category.
 * @property name The display name of the category.
 * @property imageUrl The URL for the representative image of the category.
 * @property description A brief summary or details about the types of recipes in this category.
 */
data class RecipeCategory(
    val id: String,
    val name: String,
    val imageUrl: String,
    val description: String,
)