package com.android.showoff.data.di

import com.android.showoff.data.api.RecipesApi
import com.android.showoff.data.api.RecipesRemoteApi
import com.android.showoff.data.repository.RecipesRemoteDataSource
import com.android.showoff.domain.repository.RecipesRepository
import com.android.showoff.domain.usecase.GetCategoriesUseCase
import com.android.showoff.domain.usecase.GetMealDetailsUseCase
import com.android.showoff.domain.usecase.GetMealsByCategoryUseCase
import org.koin.dsl.lazyModule

/**
 * Dependency injection module for the data layer.
 *
 * Provides instances for network API implementations and data-related services
 * using Koin's lazy loading capabilities.
 */
val dataModule = lazyModule {
    single<RecipesApi> { RecipesRemoteApi(http = get()) }
    single<RecipesRepository> { RecipesRemoteDataSource(recipesApi = get()) }

    factory { GetCategoriesUseCase(repository = get()) }
    factory { GetMealsByCategoryUseCase(repository = get()) }
    factory { GetMealDetailsUseCase(repository = get()) }
}