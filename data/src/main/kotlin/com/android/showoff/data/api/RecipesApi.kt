package com.android.showoff.data.api

import com.android.showoff.data.model.response.CategoriesResponse
import com.android.showoff.data.model.response.MealDetailsResponse
import com.android.showoff.data.model.response.MealsByCategoryResponse

/**
 * Interface defining the API endpoints for retrieving recipe and meal data.
 *
 * This API provides access to meal categories, lists of meals filtered by category,
 * and detailed information for specific meals.
 */
interface RecipesApi {

    /**
     * Fetches the list of all available meal categories from the API.
     *
     * @return The [CategoriesResponse] returned by the server.
     * @throws Throwable if the network request fails or the response cannot be parsed.
     */
    suspend fun getCategories(): CategoriesResponse

    /**
     * Fetches a list of meals that belong to the specified category.
     *
     * @param category The name of the category to filter by, for example "Seafood" or "Dessert".
     * @return The [MealsByCategoryResponse] returned by the server.
     * @throws Throwable if the network request fails or the response cannot be parsed.
     */
    suspend fun getMealsByCategory(category: String): MealsByCategoryResponse

    /**
     * Fetches detailed information for a specific meal by its unique identifier.
     *
     * @param id The unique ID of the meal to retrieve details for.
     * @return The [MealDetailsResponse] returned by the server.
     * @throws Throwable if the network request fails or the response cannot be parsed.
     */
    suspend fun getMealDetails(id: String): MealDetailsResponse
}