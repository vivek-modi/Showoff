package com.android.showoff.data.mapper

import com.android.showoff.data.model.response.MealSummaryResponse
import com.android.showoff.domain.model.RecipeSummary

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