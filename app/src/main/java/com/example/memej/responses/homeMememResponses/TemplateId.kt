package com.example.memej.responses.homeMememResponses


import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
//@TypeConverters(value = [RoomConvertor::class])
//@JsonClass(generateAdapter = true)
data class TemplateId(

    @field:Json(name = "coordinates")
    val coordinates: List<Coordinates>,

    val _id: String, // 5ebd5e6a5a360f3999c998a8

    @field:Json(name = "imageUrl")
    val imageUrl: String, // image.com

    @field:Json(name = "name")
    val name: String, // KavyaIsQueen

    @field:Json(name = "numPlaceholders")
    val numPlaceholders: Int, // 4

    @field:Json(name = "tags")
    val tags: List<String>,

    @field:Json(name = "__v")
    val v: Int,

    @field:Json(name = "textSize")
    val textSize: List<Int>, // 4

    @field:Json(name = "textColorCode")
    val textColorCode: List<String> // 4

)
