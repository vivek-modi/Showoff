package com.android.recipe.data.mapper

import com.android.recipe.data.model.response.CategoryResponse
import com.android.recipe.domain.model.RecipeCategory

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