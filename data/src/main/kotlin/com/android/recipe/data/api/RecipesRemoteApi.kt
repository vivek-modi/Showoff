package com.android.recipe.data.api

import com.android.recipe.data.model.response.CategoriesResponse
import com.android.recipe.data.model.response.MealDetailsResponse
import com.android.recipe.data.model.response.MealsByCategoryResponse
import com.android.recipe.network.ktor.KtorHttpService

/**
 * Implementation of the [RecipesApi] interface that uses [KtorHttpService] to perform network requests.
 *
 * This class provides methods to interact with the meal database API, fetching categories,
 * meals by category, and detailed information about specific meals.
 *
 * @property http The HTTP service used to execute network calls.
 */
internal class RecipesRemoteApi(
    private val http: KtorHttpService,
) : RecipesApi {

    /**
     * Fetches the list of all available meal categories from the remote API.
     *
     * @return A [CategoriesResponse] containing the list of categories.
     */
    override suspend fun getCategories(): CategoriesResponse {
        return http.get(route = "categories.php")
    }

    /**
     * Fetches a list of meals belonging to a specific category from the remote API.
     *
     * @param category The name of the category to filter meals by (e.g., "Seafood", "Chicken").
     * @return A [MealsByCategoryResponse] containing the list of meals in the specified category.
     */
    override suspend fun getMealsByCategory(category: String): MealsByCategoryResponse =
        http.get(
            route = "filter.php",
            queryParams = mapOf("c" to category),
        )

    /**
     * Fetches detailed information about a specific meal by its unique identifier.
     *
     * @param id The unique ID of the meal to retrieve details for.
     * @return A [MealDetailsResponse] containing the full details of the specified meal.
     */
    override suspend fun getMealDetails(id: String): MealDetailsResponse =
        http.get(
            route = "lookup.php",
            queryParams = mapOf("i" to id),
        )
}