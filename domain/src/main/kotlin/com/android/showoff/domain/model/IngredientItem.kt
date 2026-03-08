package com.android.showoff.domain.model

/**
 * Represents an ingredient and its corresponding measurement.
 *
 * @property name The name of the ingredient.
 * @property measure The quantity or measurement of the ingredient.
 */
data class IngredientItem(
    val name: String,
    val measure: String,
)