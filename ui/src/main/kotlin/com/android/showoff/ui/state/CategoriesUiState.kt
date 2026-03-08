package com.android.showoff.ui.state

import androidx.compose.runtime.Immutable
import com.android.showoff.domain.model.RecipeCategory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Represents the UI state for the categories screen.
 *
 * @property isLoading Indicates whether the categories are currently being loaded.
 * @property categories The [ImmutableList] of [RecipeCategory] objects to be displayed.
 * @property error An optional error message to be displayed if the loading process fails.
 */
@Immutable
data class CategoriesUiState(
    val isLoading: Boolean = false,
    val categories: ImmutableList<RecipeCategory> = persistentListOf(),
    val error: String? = null,
)