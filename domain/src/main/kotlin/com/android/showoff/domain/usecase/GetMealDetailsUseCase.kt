package com.android.showoff.domain.usecase

import com.android.showoff.domain.repository.RecipesRepository

/**
 * Use case responsible for retrieving the detailed information of a specific meal.
 *
 * This class encapsulates the business logic for fetching comprehensive meal data
 * (such as ingredients, instructions, and origin) typically by its unique identifier.
 */
class GetMealDetailsUseCase(
    private val repository: RecipesRepository,
) {
    suspend operator fun invoke(id: String) = repository.getMealDetails(id)
}