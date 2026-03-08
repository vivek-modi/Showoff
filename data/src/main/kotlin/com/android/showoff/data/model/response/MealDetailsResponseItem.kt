package com.android.showoff.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data transfer object representing the detailed information of a specific meal
 * returned from a network response.
 */
@Serializable
data class MealDetailsResponseItem(
    @SerialName("idMeal")
    val id: String,

    @SerialName("strMeal")
    val name: String,

    @SerialName("strCategory")
    val category: String? = null,

    @SerialName("strArea")
    val area: String? = null,

    @SerialName("strInstructions")
    val instructions: String? = null,

    @SerialName("strMealThumb")
    val imageUrl: String? = null,

    @SerialName("strYoutube")
    val youtubeUrl: String? = null,

    @SerialName("strSource")
    val sourceUrl: String? = null,
)