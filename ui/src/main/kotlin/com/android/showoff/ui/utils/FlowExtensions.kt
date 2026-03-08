package com.android.showoff.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Observes a [Flow] and executes the [onEvent] lambda for each emitted value as an event.
 *
 * This function is lifecycle-aware and will only collect from the flow while the
 * lifecycle is in the [Lifecycle.State.STARTED] state or higher. When the lifecycle
 * moves to a lower state, collection will be paused, and resumed when it returns
 * to the [Lifecycle.State.STARTED] state.  The [onEvent] lambda is guaranteed to
 * be called on the main thread.
 *
 * This composable utilizes [LaunchedEffect] to manage the coroutine lifecycle.
 * Changes to the [flow] or the lifecycle will result in restarting the collection.
 *
 * @param flow The [Flow] to observe.
 * @param onEvent A lambda that will be executed for each emitted value from the flow.
 *              It receives the emitted value as a parameter and should handle the event.
 */
@Composable
fun <T> ObserveAsEvent(flow: Flow<T>, onEvent: (T) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(flow, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}