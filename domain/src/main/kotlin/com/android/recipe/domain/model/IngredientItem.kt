package com.android.recipe.domain.model

/**
 * Represents an ingredient and its corresponding measurement.
 *
 * @property name The name of the ingredient.
 * @property measure The quantity or measurement of the ingredient.
 * @property imageUrl The optional URL for the ingredient image.
 */
data class IngredientItem(
    val name: String,
    val measure: String,
    val imageUrl: String? = null,
)
