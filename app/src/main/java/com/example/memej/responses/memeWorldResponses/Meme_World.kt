package com.example.memej.responses.memeWorldResponses


import com.squareup.moshi.Json

data class Meme_World(


    val _id: String, // 5ec14ed4fdba9567b44adec4
    @Json(name = "lastUpdated")
    val lastUpdated: String, // 2020-05-17T14:48:52.261Z
    @Json(name = "likedBy")
    var likedBy: List<User>,
    @Json(name = "likes")
    var likes: Int, // 0
    @Json(name = "placeholders")
    val placeholders: List<String>,
    @Json(name = "score")
    val score: Double, // 1.1
    @Json(name = "tags")
    val tags: List<String>,
    @Json(name = "templateId")
    val templateId: TemplateId,
    @Json(name = "users")
    val users: List<User>
)
