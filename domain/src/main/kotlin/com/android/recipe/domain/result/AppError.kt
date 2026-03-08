package com.android.recipe.domain.result

/**
 * Represents the different categories of failures that can occur in the application.
 *
 * This abstraction allows the domain and presentation layers to handle failures
 * without depending on network-specific exceptions or infrastructure details.
 */
sealed interface AppError {

    /**
     * Represents a client-side HTTP error (4xx).
     *
     * @property code The HTTP status code returned by the server.
     */
    data class Client(val code: Int) : AppError

    /**
     * Represents a server-side HTTP error (5xx).
     *
     * @property code The HTTP status code returned by the server.
     */
    data class Server(val code: Int) : AppError

    /** Indicates that the device has no internet connection. */
    data object NoInternet : AppError

    /** Indicates that the network request exceeded the allowed timeout. */
    data object Timeout : AppError

    /** Indicates a failure while parsing or deserializing the server response. */
    data object Serialization : AppError

    /** Indicates that a requested resource could not be found (HTTP 404). */
    data object NotFound : AppError

    /**
     * Represents an unexpected or unknown error.
     *
     * @property message Optional diagnostic information.
     */
    data class Unknown(val message: String? = null) : AppError
}
