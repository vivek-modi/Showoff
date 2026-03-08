package com.android.recipe.ui.fake

import com.android.recipe.domain.model.RecipeCategory
import com.android.recipe.domain.model.RecipeDetails
import com.android.recipe.domain.model.RecipeSummary
import com.android.recipe.domain.repository.RecipesRepository
import com.android.recipe.domain.result.AppResult

class FakeRecipesRepository : RecipesRepository {

    var categoriesResult: AppResult<List<RecipeCategory>> =
        AppResult.Success(emptyList())

    var mealsByCategoryResult: AppResult<List<RecipeSummary>> =
        AppResult.Success(emptyList())

    var mealDetailsResult: AppResult<RecipeDetails> =
        AppResult.Error(
            error = com.android.recipe.domain.result.AppError.NotFound,
            message = "Not set",
        )

    var lastRequestedCategory: String? = null
    var lastRequestedMealId: String? = null

    override suspend fun getCategories(): AppResult<List<RecipeCategory>> {
        return categoriesResult
    }

    override suspend fun getMealsByCategory(category: String): AppResult<List<RecipeSummary>> {
        lastRequestedCategory = category
        return mealsByCategoryResult
    }

    override suspend fun getMealDetails(id: String): AppResult<RecipeDetails> {
        lastRequestedMealId = id
        return mealDetailsResult
    }
}
