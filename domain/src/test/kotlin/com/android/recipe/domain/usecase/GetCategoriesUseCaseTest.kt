package com.android.recipe.domain.usecase

import com.android.recipe.domain.fake.FakeRecipesRepository
import com.android.recipe.domain.model.RecipeCategory
import com.android.recipe.domain.result.AppResult
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GetCategoriesUseCaseTest {

    @Test
    fun `invoke returns categories from repository`() = runTest {
        val fakeRepository = FakeRecipesRepository()
        val expectedCategories = listOf(
            RecipeCategory(
                id = "1",
                name = "Dessert",
                imageUrl = "image-url",
                description = "Sweet meals",
            )
        )
        fakeRepository.categoriesResult = AppResult.Success(expectedCategories)

        val useCase = GetCategoriesUseCase(repository = fakeRepository)

        val result = useCase()

        assertTrue(result is AppResult.Success)
        result as AppResult.Success

        assertEquals(expectedCategories, result.data)
    }
}