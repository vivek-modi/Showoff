@file:OptIn(FlowPreview::class)

package com.android.recipe.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.recipe.domain.result.AppResult
import com.android.recipe.domain.usecase.GetCategoriesUseCase
import com.android.recipe.ui.error.toUiMessage
import com.android.recipe.ui.event.CategoriesUiEvent
import com.android.recipe.ui.state.CategoriesUiState
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.FlowPreview
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

class CategoriesViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase,
) : ViewModel() {

    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1).apply { tryEmit(Unit) }

    private val loadingState = MutableStateFlow(true)
    private val eventChannel = Channel<String>()
    val events = eventChannel.receiveAsFlow()
    private val initialState = CategoriesUiState()
    private val categoriesFlow = refreshTrigger
        .flatMapLatest {
            flow {
                when (val result = getCategoriesUseCase()) {

                    is AppResult.Success -> {
                        emit(initialState.copy(categories = result.data.toPersistentList()))
                    }

                    is AppResult.Error -> {
                        emit(initialState.copy(error = result.error.toUiMessage()))
                    }
                }

                loadingState.value = false
            }
        }

    val uiState = combine(
        categoriesFlow,
        loadingState,
    ) { dataState, loading ->
        dataState.copy(isLoading = loading)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = initialState,
    )

    /**
     * Processes incoming UI events and updates the state or triggers side effects accordingly.
     *
     * @param event The [CategoriesUiEvent] to be handled, such as refreshing the list
     * or selecting a specific category.
     */
    fun handleUiEvent(event: CategoriesUiEvent) {
        viewModelScope.launch {
            when (event) {
                CategoriesUiEvent.Refresh -> {
                    loadingState.value = true
                    refreshTrigger.emit(Unit)
                }

                is CategoriesUiEvent.OnCategoryClicked -> {
                    eventChannel.send(event.category)
                }
            }
        }
    }
}