package com.android.showoff

import android.app.Application
import com.android.showoff.coroutine.coroutineModule
import com.android.showoff.data.di.dataModule
import com.android.showoff.network.di.networkModule
import com.android.showoff.ui.di.uiModule
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