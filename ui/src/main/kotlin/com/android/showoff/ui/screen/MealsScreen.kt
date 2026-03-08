package com.android.showoff.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.showoff.ui.R
import com.android.showoff.ui.components.AppScaffold
import com.android.showoff.ui.components.LoadingOverlay
import com.android.showoff.ui.components.RetryView
import com.android.showoff.ui.event.CategoriesUiEvent
import com.android.showoff.ui.state.CategoriesUiState
import com.android.showoff.ui.theme.spacing
import com.android.showoff.ui.viewmodel.CategoriesViewModel
import com.android.showoff.ui.viewmodel.MealsViewModel

@Composable
fun MealsScreen(
    viewModel: MealsViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val errorMessage = uiState.error?.takeIf { it.isNotEmpty() } ?: stringResource(R.string.error)


    LaunchedEffect(uiState) {
        if (uiState.error != null) {
            snackBarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short,
            )
        }
    }

    AppScaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { innerPadding ->
        if (!uiState.isLoading && uiState.error != null) {
            RetryView {
                viewModel.handleUiEvent(CategoriesUiEvent.Refresh)
            }
        }
        MealsScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = MaterialTheme.spacing.medium,
                    vertical = MaterialTheme.spacing.medium,
                )
                .consumeWindowInsets(innerPadding)
                .imePadding(),
            uiState = uiState,
            contentPadding = innerPadding,
            onAction = viewModel::handleUiEvent,
        )
    }
}

@Composable
private fun MealsScreenContent(
    uiState: CategoriesUiState,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    onAction: (CategoriesUiEvent) -> Unit,
) {
    LoadingOverlay(uiState.isLoading) {
        if (uiState.isLoading.not() && uiState.error == null) {
            Column {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = modifier,
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraMedium),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraMedium),
                ) {
                    items(
                        items = uiState.categories,
                        key = { it.id },
                    ) { category ->
                        Text(
                            text = category.name,
                            modifier = Modifier
                                .clickable {
                                    onAction(CategoriesUiEvent.OnCategoryClicked(category.name))
                                }
                                .padding(MaterialTheme.spacing.medium)
                        )
                    }
                }
            }
        }
    }
}