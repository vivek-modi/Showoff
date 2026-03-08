package com.android.showoff.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.android.showoff.ui.R
import com.android.showoff.ui.components.AppScaffold
import com.android.showoff.ui.components.LoadingOverlay
import com.android.showoff.ui.components.RetryView
import com.android.showoff.ui.event.CategoriesUiEvent
import com.android.showoff.ui.state.CategoriesUiState
import com.android.showoff.ui.theme.spacing
import com.android.showoff.ui.utils.ObserveAsEvent
import com.android.showoff.ui.viewmodel.CategoriesViewModel

/**
 * Composable that represents the Categories screen of the application.
 *
 * This screen displays a grid of categories fetched from the [viewModel]. It manages
 * the UI state, handles navigation via [onCategoryClick] when a category event occurs,
 * and displays error messages using a [SnackbarHost].
 *
 * @param onCategoryClick A lambda triggered when a specific category is selected,
 * providing the category identifier as a string.
 * @param viewModel The [CategoriesViewModel] that provides the UI state and handles business logic.
 */
@Composable
fun CategoriesScreen(
    onCategoryClick: (String) -> Unit,
    viewModel: CategoriesViewModel,
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

    ObserveAsEvent(viewModel.events) {
        onCategoryClick(it)
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
        CategoriesScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = MaterialTheme.spacing.medium,
                    vertical = MaterialTheme.spacing.small,
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
 * The primary UI content for the Categories screen.
 *
 * This composable handles the display logic for the category list, including a loading overlay
 * and an informational header section. It uses a grid layout to present categories to the user.
 *
 * @param uiState The current state of the categories UI, including the list of categories,
 * loading status, and any potential errors.
 * @param contentPadding The padding values provided by the scaffold or parent container.
 * @param modifier The [Modifier] to be applied to the layout.
 * @param onAction A callback function to handle UI events, such as clicking on a specific category.
 */
@Composable
private fun CategoriesScreenContent(
    uiState: CategoriesUiState,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    onAction: (CategoriesUiEvent) -> Unit,
) {
    LoadingOverlay(uiState.isLoading) {
        if (uiState.isLoading.not() && uiState.error == null) {
            Column {
                AppInfoSection(contentPadding = contentPadding)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = modifier,
                    contentPadding = PaddingValues(MaterialTheme.spacing.medium),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                ) {
                    items(
                        items = uiState.categories,
                        key = { it.id },
                    ) { category ->
                        CategoryGridItem(
                            title = category.name,
                            imageUrl = category.imageUrl,
                            onClick = {
                                onAction(CategoriesUiEvent.OnCategoryClicked(category.name))
                            },
                        )
                    }
                }
            }
        }
    }
}

/**
 * A composable representing an individual category item within a grid.
 *
 * It displays a category image with a gradient overlay and the category title
 * positioned at the bottom-start. The item is contained within a card that
 * supports click interactions.
 *
 * @param title The name of the category to be displayed.
 * @param imageUrl The URL of the image representing the category.
 * @param onClick A callback invoked when the category item is clicked.
 * @param modifier The [Modifier] to be applied to the card layout.
 */
@Composable
private fun CategoryGridItem(
    title: String,
    imageUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.82f),
        shape = MaterialTheme.shapes.extraLarge,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.scrim.copy(alpha = 0.1f),
                                MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f),
                                MaterialTheme.colorScheme.scrim.copy(alpha = 0.9f)
                            ),
                            startY = 300f
                        ),
                    ),
            )

            Text(
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(MaterialTheme.spacing.large),
            )
        }
    }
}

/**
 * Displays the header section of the screen, containing a greeting message,
 * a brief description, and an avatar image.
 *
 * @param contentPadding The padding values to be applied to the section,
 * typically used to account for system bars or scaffold insets.
 */
@Composable
private fun AppInfoSection(contentPadding: PaddingValues) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(contentPadding)
            .padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.small
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = stringResource(R.string.categories_header_title),
                modifier = Modifier.wrapContentWidth(),
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                )
            )
            Text(
                text = stringResource(R.string.categories_header_subtitle),
                modifier = Modifier.wrapContentWidth(),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
        }
        Image(
            painter = painterResource(R.drawable.avatar_svgrepo_com),
            alignment = Alignment.CenterEnd,
            modifier = Modifier
                .padding(top = MaterialTheme.spacing.small)
                .weight(1f),
            contentDescription = null,
        )
    }
}