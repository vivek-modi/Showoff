package com.android.recipe.ui.state

import androidx.compose.runtime.Immutable
import com.android.recipe.domain.model.RecipeCategory
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

/**
 * Represents the UI state for the categories screen.
 *
 * @property isLoading Indicates whether the categories are currently being loaded.
 * @property categories The [PersistentList] of [RecipeCategory] objects to be displayed.
 * @property error An optional error message to be displayed if the loading process fails.
 */
@Immutable
data class CategoriesUiState(
    val isLoading: Boolean = true,
    val categories: PersistentList<RecipeCategory> = persistentListOf(),
    val error: Int? = null,
)
