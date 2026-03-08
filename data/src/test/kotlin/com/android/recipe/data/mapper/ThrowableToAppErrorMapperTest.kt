package com.android.recipe.data.mapper

import com.android.recipe.data.test.createClientRequestException
import com.android.recipe.data.test.createServerResponseException
import com.android.recipe.domain.result.AppError
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.IOException

class ThrowableToAppErrorMapperTest {

    @Test
    fun `maps IOException to NoInternet`() {
        val result = IOException("No internet").toAppError()

        assertEquals(AppError.NoInternet, result.error)
    }

    @Test
    fun `maps timeout to Timeout`() {
        val result = HttpRequestTimeoutException("timeout", null).toAppError()

        assertEquals(AppError.Timeout, result.error)
    }

    @Test
    fun `maps 404 client error to NotFound`() = runTest {
        val result = createClientRequestException(HttpStatusCode.NotFound).toAppError()

        assertEquals(AppError.NotFound, result.error)
    }

    @Test
    fun `maps 400 client error to Client`() = runTest {
        val result = createClientRequestException(HttpStatusCode.BadRequest).toAppError()

        assertEquals(AppError.Client(400), result.error)
    }

    @Test
    fun `maps 500 server error to Server`() = runTest {
        val result = createServerResponseException(HttpStatusCode.InternalServerError).toAppError()

        assertEquals(AppError.Server(500), result.error)
    }

    @Test
    fun `maps unknown throwable to Unknown`() {
        val result = IllegalStateException("boom").toAppError()

        assertEquals(AppError.Unknown("boom"), result.error)
    }
}