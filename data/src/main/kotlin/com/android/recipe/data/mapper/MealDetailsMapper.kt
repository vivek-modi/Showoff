package com.android.recipe.data.mapper

import com.android.recipe.data.model.response.MealDetailsResponseItem
import com.android.recipe.domain.model.IngredientItem
import com.android.recipe.domain.model.RecipeDetails
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * Internal [Json] configuration used for parsing meal details.
 * Configured to [ignoreUnknownKeys] to ensure stability when the API response
 * contains additional fields not defined in the [MealDetailsResponseItem] model.
 */
private val mapperJson = Json {
    ignoreUnknownKeys = true
}

/**
 * Converts a [JsonObject] containing raw meal details into a domain-level [RecipeDetails] object.
 *
 * This function decodes the JSON data into a [MealDetailsResponseItem] and maps its
 * properties, including an extracted list of [IngredientItem]s, to the domain model.
 *
 * @return A [RecipeDetails] instance populated with the data from this [JsonObject].
 * @throws kotlinx.serialization.SerializationException if the JSON structure is invalid or missing required fields.
 */
fun JsonObject.toDomain(): RecipeDetails {
    val details = mapperJson.decodeFromJsonElement<MealDetailsResponseItem>(this)

    val ingredients = extractIngredients()
        .map {
            IngredientItem(
                name = it.name,
                measure = it.measure,
                imageUrl = "https://www.themealdb.com/images/ingredients/${it.name.trim()}.png",
            )
        }

    return RecipeDetails(
        id = details.id,
        name = details.name,
        category = details.category.orEmpty(),
        area = details.area.orEmpty(),
        instructions = details.instructions.orEmpty(),
        imageUrl = details.imageUrl.orEmpty(),
        youtubeUrl = details.youtubeUrl,
        sourceUrl = details.sourceUrl,
        ingredients = ingredients,
    )
}