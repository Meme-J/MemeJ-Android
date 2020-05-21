package com.example.memej.responses.homeMememResponses

import com.squareup.moshi.Json

data class Meme_Home(

    @Json(name = "_id")
    val id: String, // 5ec14e55fdba9567b44adec1
    @Json(name = "lastUpdated")
    val lastUpdated: String, // 2020-05-17T14:46:45.350Z

    //Needs convertor
    @Json(name = "likedBy")
    val likedBy: List<HomeUsers>,

    @Json(name = "likes")
    val likes: Int, // 0

    @Json(name = "numPlaceholders")
    val numPlaceholders: Int, // 4

    @Json(name = "placeholders")
    val placeholders: List<String>,

    @Json(name = "score")
    val score: Double, // 1.1

    @Json(name = "stage")
    val stage: Int, // 1


    @Json(name = "tags")
    val tags: List<String>,

    @Json(name = "templateId")
    val templateId: TemplateId,


    @Json(name = "users")
    val users: List<HomeUsers>?,

    @Json(name = "__v")
    val v: Int, // 0

    @Json(name = "workspace")
    val workspace: String // Global
)
