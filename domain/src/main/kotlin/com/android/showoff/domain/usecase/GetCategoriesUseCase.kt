package com.android.showoff.domain.usecase

import com.android.showoff.domain.repository.RecipesRepository

/**
 * Use case for retrieving the list of recipe categories.
 *
 * @property repository The [RecipesRepository] used to fetch category data.
 */
class GetCategoriesUseCase(
    private val repository: RecipesRepository,
) {
    suspend operator fun invoke() = repository.getCategories()
}