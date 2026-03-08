package com.android.recipe.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.android.recipe.ui.R
import com.android.recipe.ui.components.AppScaffold
import com.android.recipe.ui.components.LoadingOverlay
import com.android.recipe.ui.components.NavigationBackButton
import com.android.recipe.ui.components.RetryView
import com.android.recipe.ui.event.MealsUiEvent
import com.android.recipe.ui.state.MealsUiState
import com.android.recipe.ui.theme.size
import com.android.recipe.ui.theme.spacing
import com.android.recipe.ui.utils.ObserveAsEvent
import com.android.recipe.ui.viewmodel.MealsViewModel

/**
 * A screen that displays a list of meals in a grid format.
 *
 * This composable manages the UI state for the meals list, including loading states,
 * error handling with snackbars and retry views, and navigation events.
 *
 * @param onBackPressed Callback invoked when the user clicks the navigation back button.
 * @param onMealClicked Callback invoked when a specific meal is selected, passing the meal's unique ID.
 * @param viewModel The [MealsViewModel] responsible for providing the screen's state and handling UI events.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealsScreen(
    onBackPressed: () -> Unit,
    onMealClicked: (String) -> Unit,
    viewModel: MealsViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val errorMessage = uiState.error?.let { stringResource(it) } ?: stringResource(R.string.error)

    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            snackBarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short,
            )
        }
    }

    ObserveAsEvent(viewModel.events) {
        onMealClicked(it)
    }

    AppScaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(uiState.title)
                },
                navigationIcon = {
                    NavigationBackButton(onClick = onBackPressed)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
        }
    ) { innerPadding ->
        if (!uiState.isLoading && uiState.error != null) {
            RetryView {
                viewModel.handleUiEvent(MealsUiEvent.Refresh)
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

/**
 * Displays the main content of the meals screen, including a loading overlay and a grid of meals.
 *
 * @param uiState The current state of the meals screen, containing the list of meals and loading status.
 * @param contentPadding The padding values to be applied to the scrollable content, typically derived from a Scaffold.
 * @param modifier The [Modifier] to be applied to the layout.
 * @param onAction Callback to handle UI events such as selecting a meal.
 */
@Composable
private fun MealsScreenContent(
    uiState: MealsUiState,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    onAction: (MealsUiEvent) -> Unit,
) {
    LoadingOverlay(uiState.isLoading) {
        if (uiState.isLoading.not() && uiState.error == null) {
            Column {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = modifier,
                    contentPadding = contentPadding,
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraMedium),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraMedium),
                ) {
                    items(
                        items = uiState.meals,
                        key = { it.id },
                    ) { meal ->
                        MealGridItem(
                            title = meal.name,
                            imageUrl = meal.imageUrl,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                onAction(MealsUiEvent.OnMealClicked(meal.id))
                            },
                        )
                    }
                }
            }
        }
    }
}

/**
 * A single item in the meals grid, featuring a circular meal image overlapping a card.
 *
 * @param title The name or title of the meal to be displayed.
 * @param imageUrl The URL of the image representing the meal.
 * @param onClick Callback invoked when the item is clicked.
 * @param modifier The [Modifier] to be applied to this item's layout.
 */
@Composable
private fun MealGridItem(
    title: String,
    imageUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Card(
            onClick = onClick,
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.Center)
                .padding(top = MaterialTheme.spacing.extraXLarge)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = MaterialTheme.spacing.large,
                        end = MaterialTheme.spacing.large,
                        top = MaterialTheme.spacing.extraXxLarge,
                        bottom = MaterialTheme.spacing.large,
                    )
                    .height(MaterialTheme.size.SmallCard),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        AsyncImage(
            model = imageUrl,
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(MaterialTheme.size.Card * 1.2f)
                .clip(CircleShape)
                .border(
                    width = 4.dp,
                    color = MaterialTheme.colorScheme.surface,
                    shape = CircleShape,
                )
        )
    }
}
