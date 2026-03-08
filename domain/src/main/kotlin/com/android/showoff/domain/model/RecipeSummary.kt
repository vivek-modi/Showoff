package com.android.showoff.domain.model

/**
 * Represents a simplified version of a recipe, typically used for displaying in lists or overviews.
 *
 * @property id The unique identifier of the recipe.
 * @property name The display name or title of the recipe.
 * @property imageUrl The URL pointing to a representative image of the recipe.
 */
data class RecipeSummary(
    val id: String,
    val name: String,
    val imageUrl: String,
)