package com.android.recipe.ui.error

import com.android.recipe.domain.result.AppError

/**
 * Converts a domain-level [AppError] into a user-friendly message
 * suitable for display in the UI.
 *
 * The presentation layer owns these messages to keep the domain and
 * data layers independent of UI wording and localization concerns.
 */
fun AppError.toUiMessage(): String {
    return when (this) {
        AppError.NoInternet -> "No internet connection."
        AppError.Timeout -> "Request timed out. Please try again."
        AppError.Serialization -> "Something went wrong while reading the response."
        AppError.NotFound -> "Recipe not found."
        is AppError.Client -> "Something went wrong with the request."
        is AppError.Server -> "Server error. Please try again later."
        is AppError.Unknown -> "Something went wrong."
    }
}