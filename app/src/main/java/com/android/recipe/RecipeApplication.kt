package com.android.recipe

import android.app.Application
import com.android.recipe.coroutine.coroutineModule
import com.android.recipe.data.di.dataModule
import com.android.recipe.network.di.networkModule
import com.android.recipe.ui.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.lazyModules

class RecipeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initialiseKoin()
    }

    private fun initialiseKoin() {
        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@RecipeApplication)
            // Load modules
            lazyModules(
                coroutineModule,
                networkModule,
                dataModule,
                uiModule,
            )
        }
    }
}
