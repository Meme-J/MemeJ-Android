package com.example.memej.responses.memeWorldResponses

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.memej.Utils.RoomConvertor
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@TypeConverters(value = [RoomConvertor::class])
@JsonClass(generateAdapter = true)
data class TemplateId(

    @ColumnInfo(name = "template_id")
    @PrimaryKey
    var _id: String = " ", // 5ebd5e6a5a360f3999c998a8


    @field:Json(name = "imageUrl")
    var imageUrl: String = " ", // image.com

    @field:Json(name = "name")
    var name: String = " ", // KavyaIsQueen

    @field:Json(name = "numPlaceholders")
    var numPlaceholders: Int = 0, // 4

//    @Embedded
    @field:Json(name = "textSize")
    var textSize: List<Int> = listOf<Int>(), // 4


//    @Embedded
    @field:Json(name = "textColorCode")
    var textColorCode: List<String> = listOf(), // 4


//    @Embedded
    @field:Json(name = "tags")
    var tags: List<String> = listOf(),


    @field:Json(name = "coordinates")
    var coordinates: List<Coordinate> = listOf<Coordinate>(),


    @field:Json(name = "__v")
    val __v: Int = 0// 0

)