package com.android.showoff.domain.usecase

import com.android.showoff.domain.fake.FakeRecipesRepository
import com.android.showoff.domain.model.IngredientItem
import com.android.showoff.domain.model.RecipeDetails
import com.android.showoff.domain.result.AppResult
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GetMealDetailsUseCaseTest {

    @Test
    fun `invoke returns meal details from repository`() = runTest {
        val fakeRepository = FakeRecipesRepository()
        val expectedDetails = RecipeDetails(
            id = "52772",
            name = "Teriyaki Chicken Casserole",
            category = "Chicken",
            area = "Japanese",
            instructions = "Cook it well",
            imageUrl = "image-url",
            youtubeUrl = "youtube-url",
            sourceUrl = "source-url",
            ingredients = listOf(
                IngredientItem(
                    name = "Chicken",
                    measure = "500g",
                )
            ),
        )
        fakeRepository.mealDetailsResult = AppResult.Success(expectedDetails)

        val useCase = GetMealDetailsUseCase(repository = fakeRepository)

        val result = useCase("52772")

        assertTrue(result is AppResult.Success)
        result as AppResult.Success

        assertEquals(expectedDetails, result.data)
    }

    @Test
    fun `invoke passes meal id to repository`() = runTest {
        val fakeRepository = FakeRecipesRepository()
        val useCase = GetMealDetailsUseCase(repository = fakeRepository)

        useCase("abc123")

        assertEquals("abc123", fakeRepository.lastRequestedMealId)
    }
}