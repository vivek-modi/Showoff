package com.android.recipe.ui.viewmodel

import com.android.recipe.domain.model.RecipeDetails
import com.android.recipe.domain.result.AppResult
import com.android.recipe.domain.usecase.GetMealDetailsUseCase
import com.android.recipe.ui.fake.FakeRecipesRepository
import com.android.recipe.ui.util.MainDispatcherExtension
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class MealDetailsViewModelTest {

    @RegisterExtension
    @JvmField
    val mainDispatcherExtension = MainDispatcherExtension()

    private lateinit var fakeRepository: FakeRecipesRepository
    private lateinit var getMealDetailsUseCase: GetMealDetailsUseCase
    private lateinit var viewModel: MealDetailsViewModel
    private val mealId = "52772"

    @BeforeEach
    fun setUp() {
        fakeRepository = FakeRecipesRepository()
        getMealDetailsUseCase = GetMealDetailsUseCase(fakeRepository)
    }

    @Test
    fun `initial state is loading`() = runTest {
        viewModel = MealDetailsViewModel(mealId, getMealDetailsUseCase)
        assertTrue(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `successful meal details load updates uiState`() = runTest {
        val details = RecipeDetails(
            id = mealId,
            name = "Teriyaki Chicken Casserole",
            category = "Chicken",
            area = "Japanese",
            instructions = "Steps...",
            imageUrl = "url",
            youtubeUrl = "youtube",
            ingredients = emptyList(),
            sourceUrl = "source"
        )
        fakeRepository.mealDetailsResult = AppResult.Success(details)

        viewModel = MealDetailsViewModel(mealId, getMealDetailsUseCase)

        val collectJob = launch { viewModel.uiState.collect() }

        val state = viewModel.uiState.first { !it.isLoading }

        assertFalse(state.isLoading)
        assertEquals(details, state.details)
        assertEquals(null, state.error)

        collectJob.cancel()
    }

    @Test
    fun `failed meal details load updates uiState with error`() = runTest {
        val errorMessage = "Not found"
        fakeRepository.mealDetailsResult = AppResult.Error(
            error = com.android.recipe.domain.result.AppError.NotFound,
            message = errorMessage
        )

        viewModel = MealDetailsViewModel(mealId, getMealDetailsUseCase)

        val collectJob = launch { viewModel.uiState.collect() }

        val state = viewModel.uiState.first { !it.isLoading }

        assertFalse(state.isLoading)
        assertEquals(null, state.details)
        assertEquals(errorMessage, state.error)

        collectJob.cancel()
    }

    @Test
    fun `handle Refresh event reloads meal details`() = runTest {
        val details1 = RecipeDetails(id = mealId, name = "Name 1", category = "", area = "", instructions = "", imageUrl = "", youtubeUrl = "", ingredients = emptyList(), sourceUrl = "")
        fakeRepository.mealDetailsResult = AppResult.Success(details1)
        
        viewModel = MealDetailsViewModel(mealId, getMealDetailsUseCase)

        val collectJob = launch { viewModel.uiState.collect() }

        // Wait for first load
        viewModel.uiState.first { !it.isLoading }

        val details2 = RecipeDetails(id = mealId, name = "Name 2", category = "", area = "", instructions = "", imageUrl = "", youtubeUrl = "", ingredients = emptyList(), sourceUrl = "")
        fakeRepository.mealDetailsResult = AppResult.Success(details2)

        viewModel.handleUiEvent(com.android.recipe.ui.event.MealDetailsUiEvent.Refresh)

        val state = viewModel.uiState.first { !it.isLoading && it.details?.name == "Name 2" }

        assertEquals(details2, state.details)

        collectJob.cancel()
    }
}
