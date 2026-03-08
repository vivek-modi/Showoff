package com.android.recipe.network.error

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.serialization.SerializationException
import java.io.IOException

/**
 * Maps a [Throwable] to a corresponding [NetworkError].
 *
 * This extension function categorizes common network and serialization exceptions into
 * a domain-specific [NetworkError] representation, handling:
 * - 4xx Client errors via [ClientRequestException]
 * - 5xx Server errors via [ServerResponseException]
 * - Request timeouts via [HttpRequestTimeoutException]
 * - JSON parsing issues via [SerializationException]
 * - Connectivity issues via [IOException]
 *
 * @return A [NetworkError] instance describing the type of failure.
 */
fun Throwable.toNetworkError(): NetworkError {
    return when (this) {
        is ClientRequestException -> NetworkError.ClientError(code = response.status.value)

        is ServerResponseException -> NetworkError.ServerError(code = response.status.value)

        is HttpRequestTimeoutException -> NetworkError.Timeout
        is SerializationException -> NetworkError.Serialization
        is IOException -> NetworkError.NoInternet
        else -> NetworkError.Unknown(this)
    }
}
