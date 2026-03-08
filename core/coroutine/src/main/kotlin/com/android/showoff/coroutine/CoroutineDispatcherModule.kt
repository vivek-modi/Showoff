package com.android.showoff.coroutine

import org.koin.dsl.lazyModule

val coroutineModule = lazyModule {  // Or name it dispatchersModule
    single<DispatcherProvider> { DefaultDispatcherProvider() }
}