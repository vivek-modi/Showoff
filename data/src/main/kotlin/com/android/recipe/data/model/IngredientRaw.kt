package com.android.recipe.data.model

/**
 * Represents the raw data model for an ingredient as retrieved from a data source (e.g., a network API or database).
 * This class serves as a Data Transfer Object (DTO) before it is mapped to a domain-level entity.
 */
data class IngredientRaw(
    val name: String,
    val measure: String,
)
