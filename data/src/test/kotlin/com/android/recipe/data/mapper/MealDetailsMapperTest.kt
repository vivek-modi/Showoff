package com.android.recipe.data.mapper

import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MealDetailsMapperTest {

    @Test
    fun `toDomain maps JsonObject to RecipeDetails`() {
        val json = buildJsonObject {
            put("idMeal", "52772")
            put("strMeal", "Teriyaki Chicken Casserole")
            put("strCategory", "Chicken")
            put("strArea", "Japanese")
            put("strInstructions", "Cook it")
            put("strMealThumb", "image-url")
            put("strYoutube", "youtube-url")
            put("strSource", "source-url")
            put("strIngredient1", "Chicken")
            put("strMeasure1", "500g")
        }

        val result = json.toDomain()

        assertEquals("52772", result.id)
        assertEquals("Teriyaki Chicken Casserole", result.name)
        assertEquals("Chicken", result.category)
        assertEquals("Japanese", result.area)
        assertEquals("Cook it", result.instructions)
        assertEquals("image-url", result.imageUrl)
        assertEquals("youtube-url", result.youtubeUrl)
        assertEquals("source-url", result.sourceUrl)
        assertEquals(1, result.ingredients.size)
        assertEquals("Chicken", result.ingredients[0].name)
        assertEquals("500g", result.ingredients[0].measure)
        assertEquals(
            "https://www.themealdb.com/images/ingredients/Chicken.png",
            result.ingredients[0].imageUrl,
        )
    }

    @Test
    fun `toDomain uses empty strings for nullable fields`() {
        val json = buildJsonObject {
            put("idMeal", "1")
            put("strMeal", "Meal")
        }

        val result = json.toDomain()

        assertEquals("", result.category)
        assertEquals("", result.area)
        assertEquals("", result.instructions)
        assertEquals("", result.imageUrl)
    }
}
