package com.android.recipe.data.mapper

import com.android.recipe.data.model.response.MealSummaryResponse
import com.android.recipe.domain.model.RecipeSummary

/**
 * Maps a [MealSummaryResponse] data transfer object to a [RecipeSummary] domain model.
 *
 * @return A [RecipeSummary] containing the mapped meal ID, name, and image URL.
 */
fun MealSummaryResponse.toDomain(): RecipeSummary {
    return RecipeSummary(
        id = idMeal,
        name = strMeal,
        imageUrl = strMealThumb,
    )
}