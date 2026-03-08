package com.android.showoff.ui.state

import androidx.compose.runtime.Immutable
import com.android.showoff.domain.model.RecipeSummary
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

/**
 * Represents the UI state for the meals screen, capturing the current data,
 * loading status, and any potential error messages to be displayed to the user.
 *
 * @property isLoading Indicates whether the screen is currently fetching data.
 * @property title The header or category name displayed on the meals screen.
 * @property meals The persistentListOf of recipe summaries to be displayed.
 * @property error An optional error message if the data fetch fails.
 */
@Immutable
data class MealsUiState(
    val isLoading: Boolean = true,
    val title: String,
    val meals: PersistentList<RecipeSummary> = persistentListOf(),
    val error: String? = null,
)
