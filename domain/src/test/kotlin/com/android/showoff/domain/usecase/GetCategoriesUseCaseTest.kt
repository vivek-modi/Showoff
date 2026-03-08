package com.android.showoff.domain.usecase

import com.android.showoff.domain.fake.FakeRecipesRepository
import com.android.showoff.domain.model.RecipeCategory
import com.android.showoff.domain.result.AppResult
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