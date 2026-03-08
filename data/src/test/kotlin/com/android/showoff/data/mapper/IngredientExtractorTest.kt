package com.android.showoff.data.mapper

import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class IngredientExtractorTest {

    @Test
    fun `extractIngredients returns sorted trimmed ingredients`() {
        val json = buildJsonObject {
            put("strIngredient2", " Soy Sauce ")
            put("strMeasure2", " 2 tbsp ")
            put("strIngredient1", " Chicken ")
            put("strMeasure1", " 500g ")
        }

        val result = json.extractIngredients()

        assertEquals(2, result.size)

        assertEquals("Chicken", result[0].name)
        assertEquals("500g", result[0].measure)

        assertEquals("Soy Sauce", result[1].name)
        assertEquals("2 tbsp", result[1].measure)
    }

    @Test
    fun `extractIngredients ignores blank ingredient values`() {
        val json = buildJsonObject {
            put("strIngredient1", " ")
            put("strMeasure1", "1 cup")
            put("strIngredient2", "Salt")
            put("strMeasure2", "1 tsp")
        }

        val result = json.extractIngredients()

        assertEquals(1, result.size)
        assertEquals("Salt", result[0].name)
        assertEquals("1 tsp", result[0].measure)
    }

    @Test
    fun `extractIngredients returns empty list when no valid ingredients exist`() {
        val json = buildJsonObject {
            put("strIngredient1", " ")
            put("strIngredient2", "")
        }

        val result = json.extractIngredients()

        assertTrue(result.isEmpty())
    }
}