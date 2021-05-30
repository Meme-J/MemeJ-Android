package com.example.memej.models.responses.search


import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class SearchResponse(
    @field:Json(name = "suggestions")
    val suggestions: List<Suggestion>
) {

    @Keep
    data class Suggestion(
        @field:Json(name = "tag")
        val tag: String // sdalkjscoidjsado
    )
}