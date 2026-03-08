package com.android.recipe.ui.viewmodel

import com.android.recipe.domain.model.RecipeCategory
import com.android.recipe.domain.result.AppError
import com.android.recipe.domain.result.AppResult
import com.android.recipe.domain.usecase.GetCategoriesUseCase
import com.android.recipe.ui.error.toUiMessageRes
import com.android.recipe.ui.event.CategoriesUiEvent
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

class CategoriesViewModelTest {

    @RegisterExtension
    @JvmField
    val mainDispatcherExtension = MainDispatcherExtension()

    private lateinit var fakeRepository: FakeRecipesRepository
    private lateinit var getCategoriesUseCase: GetCategoriesUseCase
    private lateinit var viewModel: CategoriesViewModel

    @BeforeEach
    fun setUp() {
        fakeRepository = FakeRecipesRepository()
        getCategoriesUseCase = GetCategoriesUseCase(fakeRepository)
    }

    @Test
    fun `initial state is loading`() = runTest {
        viewModel = CategoriesViewModel(getCategoriesUseCase)
        assertTrue(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `successful categories load updates uiState`() = runTest {
        val categories = listOf(
            RecipeCategory("1", "Beef", "url", "desc")
        )
        fakeRepository.categoriesResult = AppResult.Success(categories)

        viewModel = CategoriesViewModel(getCategoriesUseCase)

        // Trigger collection to start the stateIn flow
        val collectJob = launch { viewModel.uiState.collect() }

        val state = viewModel.uiState.first { !it.isLoading }

        assertFalse(state.isLoading)
        assertEquals(categories, state.categories)
        assertEquals(null, state.error)

        collectJob.cancel()
    }

    @Test
    fun `failed categories load updates uiState with error`() = runTest {
        val error = AppError.NoInternet
        fakeRepository.categoriesResult = AppResult.Error(error)

        viewModel = CategoriesViewModel(getCategoriesUseCase)

        val collectJob = launch { viewModel.uiState.collect() }

        val state = viewModel.uiState.first { !it.isLoading }

        assertFalse(state.isLoading)
        assertTrue(state.categories.isEmpty())
        assertEquals(error.toUiMessageRes(), state.error)

        collectJob.cancel()
    }

    @Test
    fun `handle Refresh event reloads categories`() = runTest {
        fakeRepository.categoriesResult = AppResult.Success(emptyList())
        viewModel = CategoriesViewModel(getCategoriesUseCase)

        val collectJob = launch { viewModel.uiState.collect() }

        viewModel.uiState.first { !it.isLoading }

        val newCategories = listOf(RecipeCategory("2", "Chicken", "url2", "desc2"))
        fakeRepository.categoriesResult = AppResult.Success(newCategories)

        viewModel.handleUiEvent(CategoriesUiEvent.Refresh)

        val state = viewModel.uiState.first { !it.isLoading && it.categories.isNotEmpty() }

        assertEquals(newCategories, state.categories)

        collectJob.cancel()
    }

    @Test
    fun `handle OnCategoryClicked event sends category name`() = runTest {
        viewModel = CategoriesViewModel(getCategoriesUseCase)

        viewModel.handleUiEvent(CategoriesUiEvent.OnCategoryClicked("Beef"))

        val event = viewModel.events.first()
        assertEquals("Beef", event)
    }
}
