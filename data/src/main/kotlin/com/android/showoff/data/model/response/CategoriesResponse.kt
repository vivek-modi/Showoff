package com.android.showoff.data.model.response

import kotlinx.serialization.Serializable

/**
 * Data transfer object representing a list of category responses from the API.
 *
 * @property categories A list of individual [CategoryResponse] objects.
 */
@Serializable
data class CategoriesResponse(
    val categories: List<CategoryResponse>,
)

/**
 * Data transfer object representing a specific food category.
 *
 * @property idCategory The unique identifier for the category.
 * @property strCategory The name of the category.
 * @property strCategoryThumb The URL to the category's thumbnail image.
 * @property strCategoryDescription A detailed description of the category.
 */
@Serializable
data class CategoryResponse(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String,
)