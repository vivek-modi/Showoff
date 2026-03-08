package com.android.recipe.network.di

import com.android.recipe.network.ktor.KtorClientFactory
import com.android.recipe.network.ktor.KtorHttpService
import org.koin.dsl.lazyModule

/**
 * Koin module responsible for providing network-related dependencies, including
 * the Ktor HTTP client and the [KtorHttpService] for executing API requests.
 */
val networkModule = lazyModule {
    single { KtorClientFactory().create() }
    single {
        KtorHttpService(
            dispatcherProvider = get(),
            client = get(),
        )
    }
}
