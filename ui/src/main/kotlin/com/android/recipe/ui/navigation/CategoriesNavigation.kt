package com.android.recipe.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.android.recipe.ui.screen.CategoriesScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.dsl.ScopeDSL
import org.koin.dsl.navigation3.navigation

/**
 * Navigation key representing the categories screen destination.
 *
 * Used with the Navigation3 library to define the route for displaying
 * the list of available content categories.
 */
@Serializable
data object CategoriesNavigation : NavKey

/**
 * Defines the navigation route for the categories screen.
 *
 * This function handles the destination logic for [CategoriesNavigation],
 * providing the necessary UI composition when the navigation state matches this key.
 *
 * @return A navigation record or composable destination associated with the categories screen.
 */
fun ScopeDSL.categoriesRoute() {
    navigation<CategoriesNavigation> {
        val navigator = get<Navigator>()
        CategoriesScreen(
            onCategoryClick = { category ->
                navigator.goTo(MealsNavigation(category = category))
            },
            viewModel = koinViewModel(),
        )
    }
}
