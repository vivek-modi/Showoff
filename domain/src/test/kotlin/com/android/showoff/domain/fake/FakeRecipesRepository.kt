package com.android.showoff.domain.fake

import com.android.showoff.domain.model.RecipeCategory
import com.android.showoff.domain.model.RecipeDetails
import com.android.showoff.domain.model.RecipeSummary
import com.android.showoff.domain.repository.RecipesRepository
import com.android.showoff.domain.result.AppResult

class FakeRecipesRepository : RecipesRepository {

    var categoriesResult: AppResult<List<RecipeCategory>> =
        AppResult.Success(emptyList())

    var mealsByCategoryResult: AppResult<List<RecipeSummary>> =
        AppResult.Success(emptyList())

    var mealDetailsResult: AppResult<RecipeDetails> =
        AppResult.Error(
            error = com.android.showoff.domain.result.AppError.NotFound,
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