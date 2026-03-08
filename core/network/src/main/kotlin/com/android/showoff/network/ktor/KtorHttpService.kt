package com.android.showoff.network.ktor

import com.android.showoff.coroutine.DispatcherProvider
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.withContext

class KtorHttpService(val dispatcherProvider: DispatcherProvider, val client: HttpClient) {

    companion object {
        const val API_BASE_PATH = "api/json/v1/1"
    }

    /**
     * Executes a GET request for the given API route and optional query parameters.
     *
     * @param route The endpoint path relative to the API base path.
     * @param queryParams Optional query parameters to append to the request.
     * @return The deserialized response body as [Response].
     * @throws Throwable if the request fails or the response body cannot be deserialized.
     */
    suspend inline fun <reified Response : Any> get(
        route: String,
        queryParams: Map<String, String> = emptyMap(),
    ): Response {
        return withContext(dispatcherProvider.io) {
            client.get("$API_BASE_PATH/$route") {
                queryParams.forEach { (key, value) ->
                    parameter(key, value)
                }
            }.body<Response>()
        }
    }
}