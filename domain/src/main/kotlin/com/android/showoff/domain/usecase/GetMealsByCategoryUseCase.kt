package com.android.showoff.domain.usecase

import com.android.showoff.domain.repository.RecipesRepository

/**
 * Use case responsible for retrieving a list of meals filtered by a specific category.
 *
 * This class encapsulates the business logic for fetching meal data belonging to a particular
 * classification (e.g., Seafood, Dessert, Beef) from the data layer.
 */
class GetMealsByCategoryUseCase(
    private val repository: RecipesRepository,
) {
    suspend operator fun invoke(category: String) = repository.getMealsByCategory(category)
}