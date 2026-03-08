package com.android.showoff.data.mapper

import com.android.showoff.data.model.response.CategoryResponse
import com.android.showoff.domain.model.RecipeCategory

/**
 * Maps a [CategoryResponse] data transfer object from the data layer
 * to a [RecipeCategory] domain model.
 *
 * @return A [RecipeCategory] instance containing the category details.
 */
fun CategoryResponse.toDomain(): RecipeCategory {
    return RecipeCategory(
        id = idCategory,
        name = strCategory,
        imageUrl = strCategoryThumb,
        description = strCategoryDescription,
    )
}