package com.android.showoff.data.repository

import com.android.showoff.data.fake.FakeRecipesApi
import com.android.showoff.data.model.response.CategoriesResponse
import com.android.showoff.data.model.response.CategoryResponse
import com.android.showoff.data.model.response.MealDetailsResponse
import com.android.showoff.data.model.response.MealSummaryResponse
import com.android.showoff.data.model.response.MealsByCategoryResponse
import com.android.showoff.data.test.createClientRequestException
import com.android.showoff.data.test.createServerResponseException
import com.android.showoff.domain.result.AppError
import com.android.showoff.domain.result.AppResult
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.IOException

class RecipesRemoteDataSourceTest {

    private val fakeRecipesApi: FakeRecipesApi = FakeRecipesApi()
    private val repository: RecipesRemoteDataSource =
        RecipesRemoteDataSource(recipesApi = fakeRecipesApi)

    @Test
    fun `getCategories returns mapped domain categories on success`() = runTest {
        fakeRecipesApi.categoriesResponse = CategoriesResponse(
            categories = listOf(
                CategoryResponse(
                    idCategory = "1",
                    strCategory = "Beef",
                    strCategoryThumb = "thumb-url",
                    strCategoryDescription = "desc"
                )
            )
        )

        val result = repository.getCategories()

        assertTrue(result is AppResult.Success)
        result as AppResult.Success

        assertEquals(1, result.data.size)
        assertEquals("1", result.data[0].id)
        assertEquals("Beef", result.data[0].name)
        assertEquals("thumb-url", result.data[0].imageUrl)
        assertEquals("desc", result.data[0].description)
    }

    @Test
    fun `getCategories returns NoInternet when api throws IOException`() = runTest {
        fakeRecipesApi.categoriesThrowable = IOException("No internet")

        val result = repository.getCategories()

        assertTrue(result is AppResult.Error)
        result as AppResult.Error

        assertEquals(AppError.NoInternet, result.error)
    }

    @Test
    fun `getMealsByCategory returns mapped meal summaries on success`() = runTest {
        fakeRecipesApi.mealsByCategoryResponse = MealsByCategoryResponse(
            meals = listOf(
                MealSummaryResponse(
                    idMeal = "101",
                    strMeal = "Salmon",
                    strMealThumb = "meal-thumb"
                )
            )
        )

        val result = repository.getMealsByCategory("Seafood")

        assertTrue(result is AppResult.Success)
        result as AppResult.Success

        assertEquals(1, result.data.size)
        assertEquals("101", result.data[0].id)
        assertEquals("Salmon", result.data[0].name)
        assertEquals("meal-thumb", result.data[0].imageUrl)
    }

    @Test
    fun `getMealsByCategory returns empty list when meals is null`() = runTest {
        fakeRecipesApi.mealsByCategoryResponse = MealsByCategoryResponse(
            meals = null
        )

        val result = repository.getMealsByCategory("Unknown")

        assertTrue(result is AppResult.Success)
        result as AppResult.Success

        assertTrue(result.data.isEmpty())
    }

    @Test
    fun `getMealsByCategory returns timeout error when api throws timeout`() = runTest {
        fakeRecipesApi.mealsByCategoryThrowable = HttpRequestTimeoutException(
            "timeout",
            null
        )

        val result = repository.getMealsByCategory("Seafood")

        assertTrue(result is AppResult.Error)
        result as AppResult.Error

        assertEquals(AppError.Timeout, result.error)
    }

    @Test
    fun `getMealDetails returns mapped recipe details on success`() = runTest {
        fakeRecipesApi.mealDetailsResponse = MealDetailsResponse(
            meals = listOf(
                buildJsonObject {
                    put("idMeal", "52772")
                    put("strMeal", "Teriyaki Chicken Casserole")
                    put("strCategory", "Chicken")
                    put("strArea", "Japanese")
                    put("strInstructions", "Cook it well")
                    put("strMealThumb", "image-url")
                    put("strYoutube", "youtube-url")
                    put("strSource", "source-url")
                    put("strIngredient1", "Chicken")
                    put("strMeasure1", "500g")
                    put("strIngredient2", "Soy Sauce")
                    put("strMeasure2", "2 tbsp")
                }
            )
        )

        val result = repository.getMealDetails("52772")

        assertTrue(result is AppResult.Success)
        result as AppResult.Success

        assertEquals("52772", result.data.id)
        assertEquals("Teriyaki Chicken Casserole", result.data.name)
        assertEquals("Chicken", result.data.category)
        assertEquals("Japanese", result.data.area)
        assertEquals("Cook it well", result.data.instructions)
        assertEquals("image-url", result.data.imageUrl)
        assertEquals("youtube-url", result.data.youtubeUrl)
        assertEquals("source-url", result.data.sourceUrl)
        assertEquals(2, result.data.ingredients.size)
        assertEquals("Chicken", result.data.ingredients[0].name)
        assertEquals("500g", result.data.ingredients[0].measure)
        assertEquals("Soy Sauce", result.data.ingredients[1].name)
        assertEquals("2 tbsp", result.data.ingredients[1].measure)
    }

    @Test
    fun `getMealDetails returns NotFound when meals is null`() = runTest {
        fakeRecipesApi.mealDetailsResponse = MealDetailsResponse(
            meals = null
        )

        val result = repository.getMealDetails("missing-id")

        assertTrue(result is AppResult.Error)
        result as AppResult.Error

        assertEquals(AppError.NotFound, result.error)
    }

    @Test
    fun `getMealDetails returns NotFound when meals is empty`() = runTest {
        fakeRecipesApi.mealDetailsResponse = MealDetailsResponse(
            meals = emptyList()
        )

        val result = repository.getMealDetails("missing-id")

        assertTrue(result is AppResult.Error)
        result as AppResult.Error

        assertEquals(AppError.NotFound, result.error)
    }

    @Test
    fun `getMealDetails returns client error for 400`() = runTest {
        fakeRecipesApi.mealDetailsThrowable =
            createClientRequestException(HttpStatusCode.BadRequest)

        val result = repository.getMealDetails("bad-id")

        assertTrue(result is AppResult.Error)
        result as AppResult.Error

        assertEquals(AppError.Client(400), result.error)
    }

    @Test
    fun `getMealDetails returns not found for 404 client error`() = runTest {
        fakeRecipesApi.mealDetailsThrowable =
            createClientRequestException(HttpStatusCode.NotFound)

        val result = repository.getMealDetails("bad-id")

        assertTrue(result is AppResult.Error)
        result as AppResult.Error

        assertEquals(AppError.NotFound, result.error)
    }

    @Test
    fun `getMealDetails returns server error for 500`() = runTest {
        fakeRecipesApi.mealDetailsThrowable =
            createServerResponseException(HttpStatusCode.InternalServerError)

        val result = repository.getMealDetails("id")

        assertTrue(result is AppResult.Error)
        result as AppResult.Error

        assertEquals(AppError.Server(500), result.error)
    }
}