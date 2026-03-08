package com.android.recipe.domain.usecase

import com.android.recipe.domain.fake.FakeRecipesRepository
import com.android.recipe.domain.model.RecipeSummary
import com.android.recipe.domain.result.AppResult
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GetMealsByCategoryUseCaseTest {

    @Test
    fun `invoke returns meals from repository`() = runTest {
        val fakeRepository = FakeRecipesRepository()
        val expectedMeals = listOf(
            RecipeSummary(
                id = "10",
                name = "Pasta",
                imageUrl = "pasta-image",
            )
        )
        fakeRepository.mealsByCategoryResult = AppResult.Success(expectedMeals)

        val useCase = GetMealsByCategoryUseCase(repository = fakeRepository)

        val result = useCase("Italian")

        assertTrue(result is AppResult.Success)
        result as AppResult.Success

        assertEquals(expectedMeals, result.data)
    }

    @Test
    fun `invoke passes category to repository`() = runTest {
        val fakeRepository = FakeRecipesRepository()
        val useCase = GetMealsByCategoryUseCase(repository = fakeRepository)

        useCase("Seafood")

        assertEquals("Seafood", fakeRepository.lastRequestedCategory)
    }
}