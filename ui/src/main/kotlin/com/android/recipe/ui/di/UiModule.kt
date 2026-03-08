package com.android.recipe.ui.di

import com.android.recipe.ui.navigation.CategoriesNavigation
import com.android.recipe.ui.navigation.Navigator
import com.android.recipe.ui.navigation.categoriesRoute
import com.android.recipe.ui.navigation.mealsDetailsRoute
import com.android.recipe.ui.navigation.mealsRoute
import com.android.recipe.ui.viewmodel.CategoriesViewModel
import com.android.recipe.ui.viewmodel.MealDetailsViewModel
import com.android.recipe.ui.viewmodel.MealsViewModel
import org.koin.androidx.scope.dsl.activityRetainedScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.lazyModule

/**
 * Dependency injection module for the UI layer.
 *
 * This module provides ViewModels ([CategoriesViewModel], [MealsViewModel], [MealDetailsViewModel]),
 * the [Navigator], and navigation route configurations using Koin's lazy loading mechanism.
 */
@OptIn(KoinExperimentalAPI::class)
val uiModule = lazyModule {
    viewModelOf(::CategoriesViewModel)
    viewModelOf(::MealsViewModel)
    viewModelOf(::MealDetailsViewModel)

    activityRetainedScope {
        scoped { Navigator(startDestination = CategoriesNavigation) }

        categoriesRoute()

        mealsRoute()

        mealsDetailsRoute()
    }
}
