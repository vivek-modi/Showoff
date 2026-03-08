package com.android.showoff.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.android.showoff.domain.model.IngredientItem
import com.android.showoff.domain.model.RecipeDetails
import com.android.showoff.ui.R
import com.android.showoff.ui.components.AppScaffold
import com.android.showoff.ui.components.NavigationBackButton
import com.android.showoff.ui.components.RetryView
import com.android.showoff.ui.event.MealDetailsUiEvent
import com.android.showoff.ui.theme.spacing
import com.android.showoff.ui.viewmodel.MealDetailsViewModel

/**
 * Composable that displays the detailed information of a specific meal.
 *
 * This screen handles the display of meal details including its name, image, category,
 * ingredients, and cooking instructions. It also manages error states with a snackbar
 * and a retry mechanism.
 *
 * @param onBackPressed Callback invoked when the user clicks the navigation back button.
 * @param viewModel The [MealDetailsViewModel] responsible for providing the screen state and handling UI events.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailsScreen(
    onBackPressed: () -> Unit,
    viewModel: MealDetailsViewModel,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val errorMessage = uiState.error?.takeIf { it.isNotEmpty() } ?: stringResource(R.string.error)

    LaunchedEffect(uiState.error) {
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
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(uiState.details?.name.orEmpty())
                },
                navigationIcon = {
                    NavigationBackButton(onClick = onBackPressed)
                }
            )
        }
    ) { innerPadding ->
        if (!uiState.isLoading && uiState.error != null) {
            RetryView {
                viewModel.handleUiEvent(MealDetailsUiEvent.Refresh)
            }
        }
        uiState.details?.let { details ->
            MealDetailsContent(
                details = details,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = MaterialTheme.spacing.medium,
                        vertical = MaterialTheme.spacing.medium,
                    )
                    .consumeWindowInsets(innerPadding)
                    .imePadding(),
            )
        }
    }
}

/**
 * Displays the scrollable content of the meal details, including the image,
 * category information, ingredient list, and preparation instructions.
 *
 * @param details The [RecipeDetails] object containing the data to be displayed.
 * @param modifier The [Modifier] to be applied to the layout.
 */
@Composable
private fun MealDetailsContent(
    details: RecipeDetails,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        contentPadding = PaddingValues(MaterialTheme.spacing.medium),
    ) {
        item {
            AsyncImage(
                model = details.imageUrl,
                contentDescription = details.name,
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop,
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
                Text(
                    text = details.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "${details.category} • ${details.area}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        item {
            SectionTitle(title = "Ingredients")
        }

        items(details.ingredients) { ingredient ->
            IngredientRow(ingredient = ingredient)
        }

        item {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            SectionTitle(title = "Instructions")
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            Text(
                text = details.instructions,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

/**
 * A composable that represents a single row in the ingredient list.
 *
 * It displays the ingredient's name and its corresponding measurement,
 * followed by a divider.
 *
 * @param ingredient The [IngredientItem] containing the name and measure to display.
 * @param modifier The [Modifier] to be applied to the row layout.
 */
@Composable
private fun IngredientRow(
    ingredient: IngredientItem,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
    ) {
        Text(
            text = ingredient.name,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = ingredient.measure,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        HorizontalDivider()
    }
}

/**
 * A reusable composable that displays a styled title for sections within the meal details.
 *
 * @param title The text string to be displayed as the section header.
 * @param modifier The [Modifier] to be applied to the text component.
 */
@Composable
private fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier,
    )
}