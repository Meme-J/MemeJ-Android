package com.example.memej.responses


import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class SearchUserResponses(
    @Json(name = "suggestions")
    val suggestions: List<Suggestion>
) {
    data class Suggestion(
        @Json(name = "username")
        val username: String // KavyaVmatsaaaal
    )
}