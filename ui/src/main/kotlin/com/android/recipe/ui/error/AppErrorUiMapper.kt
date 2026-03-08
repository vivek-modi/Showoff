package com.android.recipe.ui.error

import androidx.annotation.StringRes
import com.android.recipe.domain.result.AppError
import com.android.recipe.ui.R

/**
 * Maps a domain-level [AppError] to a UI string resource.
 *
 * The presentation layer owns the mapping between domain errors and
 * user-facing messages. Returning a string resource instead of a raw
 * string allows proper localization and keeps the domain and data
 * layers independent from UI text.
 *
 * @return Android string resource id representing the user-friendly message.
 */
@StringRes
fun AppError.toUiMessageRes(): Int {
    return when (this) {
        AppError.NoInternet -> R.string.error_no_internet
        AppError.Timeout -> R.string.error_timeout
        AppError.Serialization -> R.string.error_serialization
        AppError.NotFound -> R.string.error_recipe_not_found
        is AppError.Client -> R.string.error_client
        is AppError.Server -> R.string.error_server
        is AppError.Unknown -> R.string.error_unknown
    }
}