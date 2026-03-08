package com.android.showoff.data.mapper

import com.android.showoff.data.model.IngredientRaw
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

/**
 * Extracts a list of ingredients and their corresponding measurements from a [JsonObject].
 *
 * This function iterates through the keys of the JSON object to find pairs of ingredients
 * and measures that share the same numeric suffix (e.g., "strIngredient1" and "strMeasure1").
 * It filters out any entries where the ingredient name is null or blank.
 *
 * @return A list of [IngredientRaw] objects, sorted by their original numeric index.
 */
internal fun JsonObject.extractIngredients(): List<IngredientRaw> {

    return keys
        .filter { it.startsWith("strIngredient") }
        .mapNotNull { key ->
            val index = key.removePrefix("strIngredient").toIntOrNull() ?: return@mapNotNull null
            Triple(index, this[key], this["strMeasure$index"])
        }.sortedBy { it.first }
        .mapNotNull { (_, ingredientValue, measureValue) ->
            val ingredient = ingredientValue
                ?.jsonPrimitive
                ?.contentOrNull
                ?.trim()
                ?.takeIf { it.isNotEmpty() }

            val measure = measureValue
                ?.jsonPrimitive
                ?.contentOrNull
                ?.trim()
                .orEmpty()

            ingredient?.let { IngredientRaw(name = it, measure = measure) }
        }
}