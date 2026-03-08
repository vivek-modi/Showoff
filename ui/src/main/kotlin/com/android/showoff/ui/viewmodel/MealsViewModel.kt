package com.android.showoff.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.showoff.domain.result.AppResult
import com.android.showoff.domain.usecase.GetMealsByCategoryUseCase
import com.android.showoff.ui.error.toUiMessage
import com.android.showoff.ui.state.MealsUiState
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class MealsViewModel(
    private val category: String,
    private val getMealsByCategoryUseCase: GetMealsByCategoryUseCase,
) : ViewModel() {
    private val refreshTrigger = MutableSharedFlow<String>(replay = 1).apply {
        tryEmit(category)
    }

    private val loadingState = MutableStateFlow(true)

    private val initialState = MealsUiState(title = category)
    private val mealsFlow = refreshTrigger.flatMapLatest { selectedCategory ->
        flow {
            loadingState.value = true

            when (val result = getMealsByCategoryUseCase(selectedCategory)) {

                is AppResult.Success -> {
                    emit(initialState.copy(meals = result.data.toPersistentList()))
                }

                is AppResult.Error -> {
                    emit(initialState.copy(error = result.error.toUiMessage()))
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
}