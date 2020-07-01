package com.example.memej.responses


import com.squareup.moshi.Json

data class SearchResponse(
    @field:Json(name = "suggestions")
    val suggestions: List<Suggestion>
) {


    data class Suggestion(
        @field:Json(name = "tag")
        val tag: String // sdalkjscoidjsado
    )
}