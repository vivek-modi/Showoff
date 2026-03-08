package com.android.showoff.data.model.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Data transfer object representing the API response for meal details.
 *
 * @property meals A nullable list of [JsonObject] containing the raw meal data.
 * Some API responses may return null when the meal is not found.
 */
@Serializable
data class MealDetailsResponse(
    val meals: List<JsonObject>? = null,
)