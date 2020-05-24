package com.example.memej.responses.memeWorldResponses

import com.squareup.moshi.Json

data class TemplateId(
    @Json(name = "coordinates")
    val coordinates: List<Coordinate>,
    @Json(name = "_id")
    val id: String, // 5ebd5e6a5a360f3999c998a8
    @Json(name = "imageUrl")
    val imageUrl: String, // image.com
    @Json(name = "name")
    val name: String, // KavyaIsQueen
    @Json(name = "numPlaceholders")
    val numPlaceholders: Int, // 4
    @Json(name = "textSize")
    val textSize: List<Any>, // 4
    @Json(name = "textColorCode")
    val textColorCode: List<Any>, // 4

    @Json(name = "tags")
    val tags: List<String>,
    @Json(name = "__v")
    val v: Int // 0
)