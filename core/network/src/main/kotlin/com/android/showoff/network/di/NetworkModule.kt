package com.android.showoff.network.di

import com.android.showoff.network.ktor.KtorClientFactory
import com.android.showoff.network.ktor.KtorHttpService
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