package com.android.showoff.domain.result

/**
 * Represents the result of an operation that may succeed or fail.
 *
 * This sealed interface is used across the application to model the outcome
 * of operations such as network requests or data loading.
 *
 * @param T The type of data returned when the operation succeeds.
 */
sealed interface AppResult<out T> {

    /**
     * Represents a successful operation containing the resulting data.
     *
     * @property data The successful result payload.
     */
    data class Success<T>(val data: T) : AppResult<T>

    /**
     * Represents a failed operation.
     *
     * @property error A structured [AppError] describing the failure type.
     * @property message Optional debug or fallback message for logging purposes.
     */
    data class Error(
        val error: AppError,
        val message: String? = null,
    ) : AppResult<Nothing>
}