package com.android.recipe.domain.repository

import com.android.recipe.domain.model.RecipeCategory
import com.android.recipe.domain.model.RecipeDetails
import com.android.recipe.domain.model.RecipeSummary
import com.android.recipe.domain.result.AppResult

/**
 * Repository interface responsible for managing recipe-related data operations.
 */
interface RecipesRepository {
    /**
     * Retrieves a list of all available recipe categories.
     *
     * @return An [AppResult] containing a list of [RecipeCategory] objects on success,
     * or an error on failure.
     */
    suspend fun getCategories(): AppResult<List<RecipeCategory>>

    /**
     * Retrieves a list of recipes belonging to a specific category.
     *
     * @param category The name of the category to filter meals by (e.g., "Seafood", "Beef").
     * @return An [AppResult] containing a list of [RecipeSummary] objects on success,
     * or an error on failure.
     */
    suspend fun getMealsByCategory(category: String): AppResult<List<RecipeSummary>>

    /**
     * Retrieves detailed information for a specific meal by its unique identifier.
     *
     * @param id The unique ID of the meal to retrieve details for.
     * @return An [AppResult] containing the [RecipeDetails] on success, or an error on failure.
     */
    suspend fun getMealDetails(id: String): AppResult<RecipeDetails>
}
