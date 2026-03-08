package com.android.recipe.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Default implementation of the [DispatcherProvider] interface.
 *
 * This provider uses the standard [Dispatchers] provided by the Kotlin Coroutines library:
 * - [io]: Map to [Dispatchers.IO] for I/O-bound tasks.
 * - [default]: Map to [Dispatchers.Default] for CPU-bound tasks.
 * - [main]: Map to [Dispatchers.Main] for UI-related tasks.
 */
class DefaultDispatcherProvider : DispatcherProvider {
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val default: CoroutineDispatcher = Dispatchers.Default
    override val main: CoroutineDispatcher = Dispatchers.Main
}
