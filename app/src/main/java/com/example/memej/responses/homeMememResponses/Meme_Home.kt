package com.example.memej.responses.homeMememResponses

import com.squareup.moshi.Json

data class Meme_Home(

    // @field:Json(name = "_id")
    val _id: String, // 5ec14e55fdba9567b44adec1
    @field:Json(name = "lastUpdated")
    val lastUpdated: String, // 2020-05-17T14:46:45.350Z

    //Needs convertor
    @field:Json(name = "likedBy")
    val likedBy: List<HomeUsers>,

    @field:Json(name = "likes")
    val likes: Int, // 0

    @field:Json(name = "numPlaceholders")
    val numPlaceholders: Int, // 4

    @field:Json(name = "placeholders")
    val placeholders: List<String>,

    @field:Json(name = "score")
    val score: Double, // 1.1

    @field:Json(name = "stage")
    val stage: Int, // 1

    @field:Json(name = "tags")
    val tags: List<String>,

    @field:Json(name = "templateId")
    val templateId: TemplateId,


    @field:Json(name = "users")
    val users: List<HomeUsers>?,

    @field:Json(name = "__v")
    val __v: Int, // 0

    @field:Json(name = "workspace")
    val workspace: String // Global
)
