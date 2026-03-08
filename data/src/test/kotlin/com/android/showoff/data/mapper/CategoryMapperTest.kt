package com.android.showoff.data.mapper

import com.android.showoff.data.model.response.CategoryResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CategoryMapperTest {

    @Test
    fun `toDomain maps CategoryResponse to RecipeCategory`() {
        val response = CategoryResponse(
            idCategory = "1",
            strCategory = "Dessert",
            strCategoryThumb = "thumb-url",
            strCategoryDescription = "sweet dishes",
        )

        val result = response.toDomain()

        assertEquals("1", result.id)
        assertEquals("Dessert", result.name)
        assertEquals("thumb-url", result.imageUrl)
        assertEquals("sweet dishes", result.description)
    }
}