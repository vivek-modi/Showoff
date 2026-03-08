package com.android.showoff.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.android.showoff.ui.screen.MealsScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.ScopeDSL
import org.koin.dsl.navigation3.navigation

/**
 * Defines the navigation structure and routes for the meals-related features of the application.
 *
 * This class typically handles the destinations, navigation arguments, and deep links
 * required to navigate between different screens in the meal category or detail flows.
 */
@Serializable
data class MealsNavigation(val category: String) : NavKey

/**
 * Defines a navigation route for displaying meals, typically based on a specific category.
 *
 * This function is used to configure a destination within the navigation graph that
 * maps to the [MealsNavigation] data class, allowing for type-safe navigation and
 * argument passing.
 */
fun ScopeDSL.mealsRoute() {
    navigation<MealsNavigation> { route ->
        val navigator = get<Navigator>()
        MealsScreen(
            onBackPressed = navigator::goBack,
            onMealClicked = { mealId ->
                navigator.goTo(MealsDetailsNavigation(mealId = mealId))
            },
            viewModel = koinViewModel { parametersOf(route.category) },
        )
    }
}