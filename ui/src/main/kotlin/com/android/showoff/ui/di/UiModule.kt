package com.android.showoff.ui.di

import com.android.showoff.ui.navigation.CategoriesNavigation
import com.android.showoff.ui.navigation.Navigator
import com.android.showoff.ui.navigation.categoriesRoute
import com.android.showoff.ui.navigation.mealsDetailsRoute
import com.android.showoff.ui.navigation.mealsRoute
import com.android.showoff.ui.viewmodel.CategoriesViewModel
import com.android.showoff.ui.viewmodel.MealDetailsViewModel
import com.android.showoff.ui.viewmodel.MealsViewModel
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