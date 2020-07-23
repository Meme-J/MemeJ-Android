package com.example.memej.responses.memeWorldResponses


import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.memej.Utils.RoomConvertor
import com.squareup.moshi.Json


@Entity
@Keep
@TypeConverters(value = [RoomConvertor::class])

data class Meme_World(

    @PrimaryKey
    val _id: String = " ", // 5ec14ed4fdba9567b44adec4

    //ColumnInfo Added to chnage only name
    @field:Json(name = "lastUpdated")
    val lastUpdated: String = " ", // 2020-05-17T14:48:52.261Z

//    @Embedded(prefix = "liked_by_")
    @field:Json(name = "likedBy")
    val likedBy: List<User> = listOf(),


    @field:Json(name = "users")
    val users: List<User> = listOf(),

    @field:Json(name = "likes")
    var likes: Int = 0, // 0

    @field:Json(name = "placeholders")
    val placeholders: List<String> = listOf(),


    @field:Json(name = "score")
    val score: Double = 0.0, // 1.1

    @field:Json(name = "tags")
    val tags: List<String> = listOf(),

    @field:Json(name = "templateId")
    val templateId: TemplateId = TemplateId()


) {
//    constructor() : this(
//        "",
//        "",
//        listOf<User>(),
//        listOf<User>(),
//        0,
//        listOf<String>(),
//        0.0,
//        listOf<String>(),
//        templateId
//    )
}