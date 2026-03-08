package com.android.showoff.network.error

/**
 * Represents the various types of failures that can occur during network operations.
 *
 * This sealed interface categorizes errors into specific types to allow for exhaustive
 * and type-safe error handling across the application.
 */
sealed interface NetworkError {
    /**
     * Represents a client-side error, typically resulting from an HTTP 4xx status code.
     *
     * @property code The specific HTTP status code returned by the server.
     */
    data class ClientError(val code: Int) : NetworkError

    /**
     * Represents a server-side error, typically resulting from an HTTP 5xx status code.
     *
     * @property code The specific HTTP status code returned by the server.
     */
    data class ServerError(val code: Int) : NetworkError

    /**
     * Represents a failure caused by the absence of an active internet connection.
     */
    data object NoInternet : NetworkError

    /**
     * Represents a failure occurring when a network request exceeds the specified time limit.
     */
    data object Timeout : NetworkError

    /**
     * Represents a failure that occurs during the data serialization or deserialization process,
     * typically when the server response cannot be parsed into the expected data model.
     */
    data object Serialization : NetworkError

    /**
     * Represents an unexpected or unidentified failure that does not fall into
     * other specific error categories.
     *
     * @property throwable The underlying cause of the error, if available.
     */
    data class Unknown(val throwable: Throwable? = null) : NetworkError
}