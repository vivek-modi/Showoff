package com.android.showoff.ui.di

import com.android.showoff.ui.navigation.CategoriesNavigation
import com.android.showoff.ui.navigation.MealsNavigation
import com.android.showoff.ui.navigation.Navigator
import com.android.showoff.ui.screen.CategoriesScreen
import com.android.showoff.ui.screen.MealsScreen
import com.android.showoff.ui.viewmodel.CategoriesViewModel
import com.android.showoff.ui.viewmodel.MealsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.scope.dsl.activityRetainedScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.lazyModule
import org.koin.dsl.navigation3.navigation

/**
 * Dependency injection module for the UI layer.
 *
 * This module provides the [CategoriesViewModel] and its required dependencies
 * using Koin's lazy loading mechanism.
 */
@OptIn(KoinExperimentalAPI::class)
val uiModule = lazyModule {
    viewModelOf(::CategoriesViewModel)
    viewModelOf(::MealsViewModel)

    activityRetainedScope {
        scoped { Navigator(startDestination = CategoriesNavigation) }

        navigation<CategoriesNavigation> {
            CategoriesScreen(
                viewModel = koinViewModel(),
            )
        }

        navigation<MealsNavigation> { route ->
            val navigator = get<Navigator>()
            MealsScreen(
                onBackPressed = navigator::goBack,
                viewModel = koinViewModel { parametersOf(route.category) }
            )
        }
    }
}