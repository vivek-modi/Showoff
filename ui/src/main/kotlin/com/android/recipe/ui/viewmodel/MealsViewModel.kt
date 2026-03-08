package com.android.recipe.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.recipe.domain.result.AppResult
import com.android.recipe.domain.usecase.GetMealsByCategoryUseCase
import com.android.recipe.ui.error.toUiMessageRes
import com.android.recipe.ui.event.MealsUiEvent
import com.android.recipe.ui.state.MealsUiState
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing and providing the state for the meals list screen
 * based on a specific category.
 *
 * It handles fetching meals from the [getMealsByCategoryUseCase], manages loading states,
 * and processes UI events such as refreshing or selecting a meal.
 *
 * @property category The name of the meal category used to filter the meals.
 * @property getMealsByCategoryUseCase The use case used to retrieve meals for the given category.
 */
class MealsViewModel(
    private val category: String,
    private val getMealsByCategoryUseCase: GetMealsByCategoryUseCase,
) : ViewModel() {
    private val refreshTrigger = MutableSharedFlow<String>(replay = 1).apply {
        tryEmit(category)
    }

    private val loadingState = MutableStateFlow(true)
    private val eventChannel = Channel<String>()
    val events = eventChannel.receiveAsFlow()

    private val initialState = MealsUiState(title = category)
    private val mealsFlow = refreshTrigger.flatMapLatest { selectedCategory ->
        flow {
            loadingState.value = true

            when (val result = getMealsByCategoryUseCase(selectedCategory)) {

                is AppResult.Success -> {
                    emit(initialState.copy(meals = result.data.toPersistentList()))
                }

                is AppResult.Error -> {
                    emit(initialState.copy(error = result.error.toUiMessageRes()))
                }
            }

            loadingState.value = false
        }
    }

    val uiState = combine(
        mealsFlow,
        loadingState,
    ) { data, loading ->
        data.copy(isLoading = loading)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = initialState
    )

    /**
     * Handles user interface events and triggers the corresponding business logic or state updates.
     *
     * This method processes:
     * - [MealsUiEvent.Refresh]: Triggers a reload of the meals for the current category.
     * - [MealsUiEvent.OnMealClicked]: Navigates to or signals an action for the selected meal category.
     *
     * @param event The [MealsUiEvent] triggered by the user in the UI.
     */
    fun handleUiEvent(event: MealsUiEvent) {
        viewModelScope.launch {
            when (event) {
                MealsUiEvent.Refresh -> refreshTrigger.emit(category)

                is MealsUiEvent.OnMealClicked -> eventChannel.send(event.category)
            }
        }
    }
}