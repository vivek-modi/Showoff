package com.android.recipe.ui.viewmodel

import com.android.recipe.domain.model.RecipeSummary
import com.android.recipe.domain.result.AppError
import com.android.recipe.domain.result.AppResult
import com.android.recipe.domain.usecase.GetMealsByCategoryUseCase
import com.android.recipe.ui.error.toUiMessageRes
import com.android.recipe.ui.event.MealsUiEvent
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

class MealsViewModelTest {

    @RegisterExtension
    @JvmField
    val mainDispatcherExtension = MainDispatcherExtension()

    private lateinit var fakeRepository: FakeRecipesRepository
    private lateinit var getMealsByCategoryUseCase: GetMealsByCategoryUseCase
    private lateinit var viewModel: MealsViewModel
    private val category = "Beef"

    @BeforeEach
    fun setUp() {
        fakeRepository = FakeRecipesRepository()
        getMealsByCategoryUseCase = GetMealsByCategoryUseCase(fakeRepository)
    }

    @Test
    fun `initial state is loading and has correct title`() = runTest {
        viewModel = MealsViewModel(category, getMealsByCategoryUseCase)
        assertTrue(viewModel.uiState.value.isLoading)
        assertEquals(category, viewModel.uiState.value.title)
    }

    @Test
    fun `successful meals load updates uiState`() = runTest {
        val meals = listOf(
            RecipeSummary("1", "Beef and Mustard Pie", "url")
        )
        fakeRepository.mealsByCategoryResult = AppResult.Success(meals)

        viewModel = MealsViewModel(category, getMealsByCategoryUseCase)

        val collectJob = launch { viewModel.uiState.collect() }

        val state = viewModel.uiState.first { !it.isLoading }

        assertFalse(state.isLoading)
        assertEquals(meals, state.meals)
        assertEquals(null, state.error)

        collectJob.cancel()
    }

    @Test
    fun `failed meals load updates uiState with error`() = runTest {
        val error = AppError.NoInternet
        fakeRepository.mealsByCategoryResult = AppResult.Error(error)

        viewModel = MealsViewModel(category, getMealsByCategoryUseCase)

        val collectJob = launch { viewModel.uiState.collect() }

        val state = viewModel.uiState.first { !it.isLoading }

        assertFalse(state.isLoading)
        assertTrue(state.meals.isEmpty())
        assertEquals(error.toUiMessageRes(), state.error)

        collectJob.cancel()
    }

    @Test
    fun `handle Refresh event reloads meals`() = runTest {
        fakeRepository.mealsByCategoryResult = AppResult.Success(emptyList())
        viewModel = MealsViewModel(category, getMealsByCategoryUseCase)

        val collectJob = launch { viewModel.uiState.collect() }

        // Wait for first load to finish
        viewModel.uiState.first { !it.isLoading }

        val newMeals = listOf(RecipeSummary("2", "Beef Stroganoff", "url2"))
        fakeRepository.mealsByCategoryResult = AppResult.Success(newMeals)

        viewModel.handleUiEvent(MealsUiEvent.Refresh)

        val state = viewModel.uiState.first { !it.isLoading && it.meals.isNotEmpty() }

        assertEquals(newMeals, state.meals)

        collectJob.cancel()
    }

    @Test
    fun `handle OnMealClicked event sends meal name`() = runTest {
        viewModel = MealsViewModel(category, getMealsByCategoryUseCase)

        viewModel.handleUiEvent(MealsUiEvent.OnMealClicked("Beef and Mustard Pie"))

        val event = viewModel.events.first()
        assertEquals("Beef and Mustard Pie", event)
    }
}
