package com.android.recipe.data.mapper

import com.android.recipe.data.model.response.MealSummaryResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MealSummaryMapperTest {

    @Test
    fun `toDomain maps MealSummaryResponse to RecipeSummary`() {
        val response = MealSummaryResponse(
            idMeal = "42",
            strMeal = "Pizza",
            strMealThumb = "image-url",
        )

        val result = response.toDomain()

        assertEquals("42", result.id)
        assertEquals("Pizza", result.name)
        assertEquals("image-url", result.imageUrl)
    }
}