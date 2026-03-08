package com.android.showoff.coroutine

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Interface to provide [CoroutineDispatcher] instances for different types of execution.
 *
 * This abstraction allows for easier testing by enabling the injection of test dispatchers
 * (like `UnconfinedTestDispatcher`) instead of the standard production dispatchers.
 */
interface DispatcherProvider {
    /**
     * Dispatcher intended for I/O-bound tasks.
     *
     * Used for operations that spend most of their time waiting for I/O, such as
     * network requests, disk read/write operations, or database queries.
     */
    val io: CoroutineDispatcher

    /**
     * Dispatcher intended for CPU-intensive tasks.
     *
     * Used for operations that perform significant computational work, such as
     * sorting large lists, parsing complex JSON, or performing image processing.
     */
    val default: CoroutineDispatcher

    /**
     * Dispatcher intended for UI-related tasks and interactions with the main thread.
     *
     * Used for operations that must execute on the main thread, such as updating UI elements,
     * responding to user input, or interacting with Android Framework classes.
     */
    val main: CoroutineDispatcher
}