package com.android.recipe.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.android.recipe.ui.screen.MealDetailsScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.ScopeDSL
import org.koin.dsl.navigation3.navigation

/**
 * Navigation destination key for the meal details screen.
 *
 * @property mealId The unique identifier of the meal to be displayed.
 */
@Serializable
data class MealsDetailsNavigation(val mealId: String) : NavKey

/**
 * Defines a navigation route for the meal details screen.
 *
 * This function typically provides the mapping between the [MealsDetailsNavigation]
 * destination key and the corresponding UI component in the navigation graph.
 */
fun ScopeDSL.mealsDetailsRoute() {
    navigation<MealsDetailsNavigation> { route ->
        val navigator = get<Navigator>()
        MealDetailsScreen(
            onBackPressed = navigator::goBack,
            viewModel = koinViewModel { parametersOf(route.mealId) },
        )
    }
}