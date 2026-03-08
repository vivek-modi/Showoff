package com.android.recipe.data.mapper

import com.android.recipe.data.model.response.MealDetailsResponseItem
import com.android.recipe.domain.model.IngredientItem
import com.android.recipe.domain.model.RecipeDetails
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
                imageUrl = ingredientImageUrl(it.name),
            )
        }

    return RecipeDetails(
        id = details.id,
        name = details.name,
        category = details.category.orEmpty(),
        area = details.area.orEmpty(),
        instructions = details.instructions?.toCleanInstructions().orEmpty(),
        imageUrl = details.imageUrl.orEmpty(),
        youtubeUrl = details.youtubeUrl,
        sourceUrl = details.sourceUrl,
        ingredients = ingredients,
    )
}

/**
 * Cleans and formats raw instruction strings by removing carriage returns,
 * replacing specific symbols (like '▢') with newlines, consolidating
 * multiple consecutive newlines into a single one, and trimming whitespace.
 *
 * @return A formatted and sanitized instruction string.
 */
private fun String.toCleanInstructions(): String {
    return replace("\r", "")
        .replace("▢", "\n")
        .replace(Regex("\n{2,}"), "\n")
        .trim()
}

/**
 * Generates a URL for the image of a given ingredient name.
 *
 * This function trims the ingredient name, URL-encodes it to ensure it is
 * safe for use in a web address, and appends it to the TheMealDB image base URL.
 *
 * @param name The name of the ingredient (e.g., "Chicken", "Olive Oil").
 * @return A complete URL string pointing to the ingredient's image.
 */
private fun ingredientImageUrl(name: String): String {
    val encoded = URLEncoder.encode(name.trim(), StandardCharsets.UTF_8.toString())
        .replace("+", "%20")
    return "https://www.themealdb.com/images/ingredients/$encoded.png"
}