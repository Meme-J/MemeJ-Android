package com.example.memej.responses.memeWorldResponses


import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class Meme_World(


    val _id: String, // 5ec14ed4fdba9567b44adec4
    @field:Json(name = "lastUpdated")
    val lastUpdated: String, // 2020-05-17T14:48:52.261Z
    @field:Json(name = "likedBy")
    val likedBy: List<User>,
    @field:Json(name = "likes")
    var likes: Int, // 0
    @field:Json(name = "placeholders")
    val placeholders: List<String>,
    @field:Json(name = "score")
    val score: Double, // 1.1
    @field:Json(name = "tags")
    val tags: List<String>,
    @field:Json(name = "templateId")
    val templateId: TemplateId,
    @field:Json(name = "users")
    val users: List<User>
)
