package com.example.memej.responses.template


import com.example.memej.responses.homeMememResponses.Coordinates
import com.squareup.moshi.Json

data class EmptyTemplateResponse(
    @Json(name = "templates")
    val templates: List<Template>
) {
    data class Template(
        @Json(name = "coordinates")
        val coordinates: List<Coordinates>,
        @Json(name = "_id")
        val _id: String, // 5ed672e0942f0f11e7927fb4
        @Json(name = "imageUrl")
        val imageUrl: String, // image.com
        @Json(name = "name")
        val name: String, // Kavya
        @Json(name = "numPlaceholders")
        val numPlaceholders: Int, // 4
        @Json(name = "tags")
        val tags: List<String>,
        @Json(name = "textColorCode")
        val textColorCode: List<String>,
        @Json(name = "textSize")
        val textSize: List<Int>,
        @Json(name = "__v")
        val v: Int // 0
    )
}