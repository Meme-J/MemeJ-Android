package com.example.memej.responses.template


import com.example.memej.responses.homeMememResponses.Coordinates
import com.squareup.moshi.Json

data class EmptyTemplateResponse(
    @field:Json(name = "templates")
    val templates: List<Template>
) {
    data class Template(
        @field:Json(name = "coordinates")
        val coordinates: List<Coordinates>,
        @field:Json(name = "_id")
        val _id: String, // 5ed672e0942f0f11e7927fb4
        @field:Json(name = "imageUrl")
        val imageUrl: String, // image.com
        @field:Json(name = "name")
        val name: String, // Kavya
        @field:Json(name = "numPlaceholders")
        val numPlaceholders: Int, // 4
        @field:Json(name = "tags")
        val tags: List<String>,
        @field:Json(name = "textColorCode")
        val textColorCode: List<String>,
        @field:Json(name = "textSize")
        val textSize: List<Int>,
        @field:Json(name = "__v")
        val v: Int // 0
    )
}