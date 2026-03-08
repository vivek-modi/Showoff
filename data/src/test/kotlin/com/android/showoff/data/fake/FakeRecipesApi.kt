package com.android.showoff.data.fake

import com.android.showoff.data.api.RecipesApi
import com.android.showoff.data.model.response.CategoriesResponse
import com.android.showoff.data.model.response.MealDetailsResponse
import com.android.showoff.data.model.response.MealsByCategoryResponse

class FakeRecipesApi : RecipesApi {

    var categoriesResponse: CategoriesResponse? = null
    var mealsByCategoryResponse: MealsByCategoryResponse? = null
    var mealDetailsResponse: MealDetailsResponse? = null

    var categoriesThrowable: Throwable? = null
    var mealsByCategoryThrowable: Throwable? = null
    var mealDetailsThrowable: Throwable? = null

    override suspend fun getCategories(): CategoriesResponse {
        categoriesThrowable?.let { throw it }
        return requireNotNull(categoriesResponse) {
            "categoriesResponse must be set before calling getCategories()"
        }
    }

    override suspend fun getMealsByCategory(category: String): MealsByCategoryResponse {
        mealsByCategoryThrowable?.let { throw it }
        return requireNotNull(mealsByCategoryResponse) {
            "mealsByCategoryResponse must be set before calling getMealsByCategory()"
        }
    }

    override suspend fun getMealDetails(id: String): MealDetailsResponse {
        mealDetailsThrowable?.let { throw it }
        return requireNotNull(mealDetailsResponse) {
            "mealDetailsResponse must be set before calling getMealDetails()"
        }
    }
}