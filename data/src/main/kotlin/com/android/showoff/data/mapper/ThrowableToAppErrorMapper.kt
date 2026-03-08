package com.android.showoff.data.mapper

import com.android.showoff.domain.result.AppError
import com.android.showoff.domain.result.AppResult
import com.android.showoff.network.error.NetworkError
import com.android.showoff.network.error.toNetworkError

/**
 * Converts a thrown [Throwable] into a domain-level [AppResult.Error].
 *
 * This function maps low-level networking failures into stable [AppError] types
 * used across the application. It ensures that the data layer does not expose
 * raw exceptions to higher layers.
 *
 * The mapping flow is:
 *
 * Throwable → NetworkError → AppError → AppResult.Error
 *
 * The returned [AppResult.Error] contains:
 * - a stable [AppError] type used for business logic
 * - an optional debug message useful for logging or diagnostics
 */
fun Throwable.toAppError(): AppResult.Error {
    val networkError = toNetworkError()

    val appError = when (networkError) {
        is NetworkError.ClientError -> {
            if (networkError.code == 404) {
                AppError.NotFound
            } else {
                AppError.Client(networkError.code)
            }
        }

        is NetworkError.ServerError -> AppError.Server(networkError.code)
        NetworkError.NoInternet -> AppError.NoInternet
        NetworkError.Timeout -> AppError.Timeout
        NetworkError.Serialization -> AppError.Serialization
        is NetworkError.Unknown -> AppError.Unknown(networkError.throwable?.message)
    }

    return AppResult.Error(
        error = appError,
        message = networkError.debugMessage(),
    )
}

/**
 * Generates a short debug message for logging or diagnostics.
 *
 * These messages are intentionally generic because final UI text
 * should be produced in the presentation layer.
 */
private fun NetworkError.debugMessage(): String {
    return when (this) {
        is NetworkError.ClientError -> "Client error ($code)"
        is NetworkError.ServerError -> "Server error ($code)"
        NetworkError.NoInternet -> "No internet connection"
        NetworkError.Timeout -> "Request timed out"
        NetworkError.Serialization -> "Failed to parse response"
        is NetworkError.Unknown -> throwable?.message ?: "Unknown error"
    }
}