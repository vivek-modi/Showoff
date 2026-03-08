package com.android.recipe.data.repository

import com.android.recipe.data.api.RecipesApi
import com.android.recipe.data.mapper.toAppError
import com.android.recipe.data.mapper.toDomain
import com.android.recipe.domain.model.RecipeCategory
import com.android.recipe.domain.model.RecipeDetails
import com.android.recipe.domain.model.RecipeSummary
import com.android.recipe.domain.repository.RecipesRepository
import com.android.recipe.domain.result.AppError
import com.android.recipe.domain.result.AppResult

/**
 * Remote implementation of the [RecipesRepository] that fetches recipe data from the [RecipesApi].
 *
 * This data source handles network requests to retrieve recipe categories, lists of meals
 * by category, and specific meal details. API responses are mapped into domain models,
 * and failures are exposed as [AppResult.Error].
 *
 * @property recipesApi The API service interface used to fetch remote recipe data.
 */
class RecipesRemoteDataSource(
    private val recipesApi: RecipesApi,
) : RecipesRepository {

    /**
     * Fetches the list of all available recipe categories from the remote API.
     *
     * @return An [AppResult] containing a list of [RecipeCategory] on success,
     * or an [AppResult.Error] if the request fails.
     */
    override suspend fun getCategories(): AppResult<List<RecipeCategory>> {
        return runCatching {
            recipesApi.getCategories()
        }.fold(
            onSuccess = { response ->
                AppResult.Success(data = response.categories.map { it.toDomain() })
            },
            onFailure = { throwable ->
                throwable.toAppError()
            },
        )
    }

    /**
     * Fetches the meals belonging to the given category from the remote API.
     *
     * @param category The category used to filter meals, for example "Seafood" or "Beef".
     * @return An [AppResult] containing a list of [RecipeSummary] on success,
     * or an [AppResult.Error] if the request fails.
     */
    override suspend fun getMealsByCategory(category: String): AppResult<List<RecipeSummary>> {
        return runCatching {
            recipesApi.getMealsByCategory(category)
        }.fold(
            onSuccess = { response ->
                AppResult.Success(data = response.meals.orEmpty().map { it.toDomain() })
            },
            onFailure = { throwable ->
                throwable.toAppError()
            },
        )
    }

    /**
     * Fetches the detailed information for a specific recipe by its unique identifier.
     *
     * If the API response does not contain any meal details, this method returns
     * [AppError.NotFound].
     *
     * @param id The unique identifier of the meal to retrieve.
     * @return An [AppResult] containing [RecipeDetails] on success,
     * or an [AppResult.Error] if the recipe is not found or the request fails.
     */
    override suspend fun getMealDetails(id: String): AppResult<RecipeDetails> {
        return runCatching {
            recipesApi.getMealDetails(id)
        }.fold(
            onSuccess = { response ->
                val meal = response.meals?.firstOrNull()

                if (meal == null) {
                    AppResult.Error(
                        error = AppError.NotFound,
                        message = "Recipe details not found."
                    )
                } else {
                    AppResult.Success(data = meal.toDomain())
                }
            },
            onFailure = { throwable ->
                throwable.toAppError()
            },
        )
    }
}