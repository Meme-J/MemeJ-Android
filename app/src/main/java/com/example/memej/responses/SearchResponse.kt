package com.example.memej.responses


import com.squareup.moshi.Json

data class SearchResponse(
    @Json(name = "suggestions")
    val suggestions: List<Suggestion>
) {
    data class Suggestion(
        @Json(name = "tag")
        val tag: String // sdalkjscoidjsado
    )
}