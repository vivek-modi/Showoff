package com.android.showoff.data.test

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

suspend fun createClientRequestException(statusCode: HttpStatusCode): ClientRequestException {
    require(statusCode.value in 400..499) {
        "createClientRequestException requires a 4xx status code."
    }

    val client = HttpClient(MockEngine) {
        expectSuccess = true
        engine {
            addHandler {
                respond(
                    content = """{"error":"client error"}""",
                    status = statusCode,
                    headers = headersOf(
                        HttpHeaders.ContentType,
                        ContentType.Application.Json.toString()
                    ),
                )
            }
        }
    }

    return try {
        client.get("https://test.local/error").bodyAsText()
        error("Expected ClientRequestException for status $statusCode")
    } catch (exception: ClientRequestException) {
        exception
    } finally {
        client.close()
    }
}

suspend fun createServerResponseException(statusCode: HttpStatusCode): ServerResponseException {
    require(statusCode.value in 500..599) {
        "createServerResponseException requires a 5xx status code."
    }

    val client = HttpClient(MockEngine) {
        expectSuccess = true
        engine {
            addHandler {
                respond(
                    content = """{"error":"server error"}""",
                    status = statusCode,
                    headers = headersOf(
                        HttpHeaders.ContentType,
                        ContentType.Application.Json.toString()
                    ),
                )
            }
        }
    }

    return try {
        client.get("https://test.local/error").bodyAsText()
        error("Expected ServerResponseException for status $statusCode")
    } catch (exception: ServerResponseException) {
        exception
    } finally {
        client.close()
    }
}