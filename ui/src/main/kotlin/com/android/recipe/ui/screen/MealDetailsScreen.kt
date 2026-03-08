package com.android.recipe.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.android.recipe.domain.model.IngredientItem
import com.android.recipe.domain.model.RecipeDetails
import com.android.recipe.ui.R
import com.android.recipe.ui.components.AppScaffold
import com.android.recipe.ui.components.LoadingOverlay
import com.android.recipe.ui.components.RetryView
import com.android.recipe.ui.event.MealDetailsUiEvent
import com.android.recipe.ui.theme.elevation
import com.android.recipe.ui.theme.size
import com.android.recipe.ui.theme.spacing
import com.android.recipe.ui.utils.staggeredEntrance
import com.android.recipe.ui.viewmodel.MealDetailsViewModel

private const val HeroHeightFactor = 0.42f
private const val GradientHeightFactor = 0.3f
private const val ActionButtonWidthFactor = 0.85f

/**
 * Composable that displays the detailed information of a specific meal with an immersive UI.
 *
 * This screen features a hero image, a bottom-sheet style content area, and staggered entrance
 * animations for a modern look and feel.
 *
 * @param onBackPressed Callback invoked when the user clicks the navigation back button.
 * @param viewModel The [MealDetailsViewModel] responsible for providing the screen state and handling UI events.
 */
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
    ) { _ ->
        LoadingOverlay(isLoading = uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState.error != null) {
                    RetryView {
                        viewModel.handleUiEvent(MealDetailsUiEvent.Refresh)
                    }
                }

                uiState.details?.let { details ->
                    MealDetailsContent(
                        details = details,
                        onBackPressed = onBackPressed,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}

/**
 * Displays the scrollable content of the meal details with a hero image and rounded content area.
 */
@Composable
private fun MealDetailsContent(
    details: RecipeDetails,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    val youtubeUrl = details.youtubeUrl

    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val screenHeight = with(density) { windowInfo.containerSize.height.toDp() }
    
    val heroHeight = screenHeight * HeroHeightFactor
    val spacerHeight = heroHeight - MaterialTheme.spacing.extraLarge
    val gradientHeight = heroHeight * GradientHeightFactor
    val actionButtonHeight = MaterialTheme.spacing.extraXLarge

    Box(modifier = modifier) {
        MealHeroSection(
            imageUrl = details.imageUrl,
            name = details.name,
            heroHeight = heroHeight,
            gradientHeight = gradientHeight,
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = actionButtonHeight * 2),
        ) {
            item {
                Spacer(modifier = Modifier.height(spacerHeight))
            }
            item {
                MealDetailsHeader(details = details)
            }

            itemsIndexed(
                items = details.ingredients,
                key = { index, it -> it.name + it.measure + index },
            ) { index, ingredient ->
                Surface(color = MaterialTheme.colorScheme.surface) {
                    IngredientItemRow(
                        ingredient = ingredient,
                        modifier = Modifier
                            .padding(
                                horizontal = MaterialTheme.spacing.large,
                                vertical = MaterialTheme.spacing.extraSmall,
                            )
                            .staggeredEntrance(index = 3 + index),
                    )
                }
            }

            item {
                MealInstructionsSection(
                    instructions = details.instructions,
                    ingredientsCount = details.ingredients.size,
                )
            }
        }

        MealFloatingBackButton(onBackPressed = onBackPressed)

        if (!youtubeUrl.isNullOrEmpty()) {
            MealWatchVideoButton(
                modifier = Modifier.align(Alignment.BottomCenter),
                actionButtonHeight = actionButtonHeight,
                onClick = { uriHandler.openUri(youtubeUrl) },
            )
        }
    }
}

/**
 * Displays the hero image and a top gradient overlay.
 */
@Composable
private fun MealHeroSection(
    imageUrl: String,
    name: String,
    heroHeight: Dp,
    gradientHeight: Dp,
) {
    Box {
        AsyncImage(
            model = imageUrl,
            contentDescription = name,
            modifier = Modifier
                .fillMaxWidth()
                .height(heroHeight),
            contentScale = ContentScale.Crop,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(gradientHeight)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.35f),
                            Color.Transparent,
                        ),
                    ),
                ),
        )
    }
}

/**
 * Displays the header information of the meal, including name, category, and origin.
 */
@Composable
private fun MealDetailsHeader(details: RecipeDetails) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(
            topStart = MaterialTheme.spacing.extraLarge,
            topEnd = MaterialTheme.spacing.extraLarge,
        ),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = MaterialTheme.elevation.extraSmall,
    ) {
        Column(
            modifier = Modifier
                .padding(MaterialTheme.spacing.large),
        ) {
            Box(
                modifier = Modifier
                    .width(MaterialTheme.spacing.extraLarge + MaterialTheme.spacing.small)
                    .height(MaterialTheme.size.extraSmall)
                    .clip(RoundedCornerShape(MaterialTheme.size.extraSmall / 2))
                    .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    .align(Alignment.CenterHorizontally),
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Text(
                text = details.name,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp,
                ),
                modifier = Modifier.staggeredEntrance(index = 0),
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
            Text(
                text = "${details.category} • ${details.area}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.staggeredEntrance(index = 1),
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            Text(
                text = stringResource(R.string.meal_ingredients_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.staggeredEntrance(index = 2),
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }
}

/**
 * Displays the step-by-step cooking directions for the recipe.
 *
 * @param instructions The raw instruction text to be converted into steps.
 * @param ingredientsCount Used to offset the staggered animation delay.
 */
@Composable
private fun MealInstructionsSection(
    instructions: String,
    ingredientsCount: Int,
) {
    val steps = remember(instructions) { instructions.toInstructionSteps() }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier.padding(MaterialTheme.spacing.large),
        ) {
            Text(
                text = stringResource(R.string.meal_instructions_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.staggeredEntrance(index = 4 + ingredientsCount),
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            steps.forEachIndexed { index, step ->
                Row(
                    modifier = Modifier
                        .padding(bottom = MaterialTheme.spacing.medium)
                        .staggeredEntrance(index = 5 + ingredientsCount + index),
                    verticalAlignment = Alignment.Top,
                ) {

                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(MaterialTheme.spacing.extraLarge),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = (index + 1).toString(),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

                    Text(
                        text = step,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 24.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

/**
 * Normalizes and splits instruction text into individual préparatory steps.
 */
private fun String.toInstructionSteps(): List<String> {
    return replace("\r", "")
        .replace("▢", "\n")
        .split("\n")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
}

/**
 * A circular back button that floats over the hero image.
 */
@Composable
private fun BoxScope.MealFloatingBackButton(onBackPressed: () -> Unit) {
    Surface(
        shape = CircleShape,
        color = Color.White.copy(alpha = 0.9f),
        modifier = Modifier
            .padding(MaterialTheme.spacing.medium)
            .statusBarsPadding()
            .size(MaterialTheme.spacing.extraLarge + MaterialTheme.spacing.extraMedium)
            .align(Alignment.TopStart),
        onClick = onBackPressed,
        shadowElevation = MaterialTheme.elevation.medium,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = stringResource(R.string.back_arrow_content_description),
                tint = Color.Black,
                modifier = Modifier.size(MaterialTheme.size.large),
            )
        }
    }
}

/**
 * A prominent floating button to trigger the video tutorial.
 */
@Composable
private fun MealWatchVideoButton(
    modifier: Modifier = Modifier,
    actionButtonHeight: Dp,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(bottom = MaterialTheme.spacing.extraLarge)
            .height(actionButtonHeight)
            .fillMaxWidth(ActionButtonWidthFactor)
            .navigationBarsPadding(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        shape = MaterialTheme.shapes.extraLarge,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = MaterialTheme.elevation.large),
    ) {
        Text(
            text = stringResource(R.string.watch_video),
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp,
            ),
        )
    }
}

/**
 * Displays a single ingredient card with its name, measurement, and a leading badge.
 */
@Composable
private fun IngredientItemRow(
    ingredient: IngredientItem,
    modifier: Modifier = Modifier,
) {
    var isImageError by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
        ),
    ) {
        Row(
            modifier = Modifier.padding(MaterialTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = RoundedCornerShape(MaterialTheme.spacing.small),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                modifier = Modifier.size(MaterialTheme.size.small * 5),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    val imageUrl = ingredient.imageUrl
                    if (imageUrl != null && !isImageError) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = ingredient.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit,
                            onError = { isImageError = true },
                        )
                    } else {
                        Text(
                            text = ingredient.name.take(1).uppercase(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.ExtraBold,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

            Text(
                text = ingredient.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
            )

            Text(
                text = ingredient.measure,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}
