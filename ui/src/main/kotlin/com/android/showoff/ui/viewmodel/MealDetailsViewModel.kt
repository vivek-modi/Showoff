package com.android.showoff.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.showoff.domain.result.AppResult
import com.android.showoff.domain.usecase.GetMealDetailsUseCase
import com.android.showoff.ui.event.MealDetailsUiEvent
import com.android.showoff.ui.state.MealDetailsUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing the state and business logic of the Meal Details screen.
 *
 * It fetches detailed information for a specific meal identified by [mealId] using the
 * [getMealDetailsUseCase] and exposes the result as a stream of [MealDetailsUiState].
 *
 * @property mealId The unique identifier of the meal to display.
 * @property getMealDetailsUseCase The use case used to retrieve meal details from the data layer.
 */
class MealDetailsViewModel(
    private val mealId: String,
    private val getMealDetailsUseCase: GetMealDetailsUseCase,
) : ViewModel() {

    private val refreshTrigger = MutableSharedFlow<String>(replay = 1).apply {
        tryEmit(mealId)
    }

    private val loadingState = MutableStateFlow(true)
    private val initialState = MealDetailsUiState()

    private val detailsFlow = refreshTrigger.flatMapLatest { selectedMealId ->
        flow {
            loadingState.value = true

            when (val result = getMealDetailsUseCase(selectedMealId)) {
                is AppResult.Success -> {
                    emit(
                        initialState.copy(
                            details = result.data,
                            error = null,
                        )
                    )
                }

                is AppResult.Error -> {
                    emit(
                        initialState.copy(
                            details = null,
                            error = result.message,
                        )
                    )
                }
            }

            loadingState.value = false
        }
    }

    val uiState = combine(
        detailsFlow,
        loadingState,
    ) { dataState, isLoading ->
        dataState.copy(isLoading = isLoading)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = initialState,
    )

    /**
     * Handles user interface events sent from the view.
     *
     * This method processes different [MealDetailsUiEvent] types, such as triggering
     * a data refresh, and performs the necessary business logic updates.
     *
     * @param event The [MealDetailsUiEvent] to be processed.
     */
    fun handleUiEvent(event: MealDetailsUiEvent) {
        viewModelScope.launch {
            when (event) {
                MealDetailsUiEvent.Refresh -> refreshTrigger.emit(mealId)
            }
        }
    }
}