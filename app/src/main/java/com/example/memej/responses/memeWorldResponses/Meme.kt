package com.example.memej.responses.memeWorldResponses

import com.squareup.moshi.Json

data class Meme(
    @Json(name = "_id")
    val id: String, // 5ebb30eaa8a8b52f72cd636f
    @Json(name = "imageUrl")
    val imageUrl: String,
    @Json(name = "lastUpdated")
    val lastUpdated: String, // 2020-05-12T23:27:38.303Z
    @Json(name = "liked")
    val liked: Boolean, // true
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
    val templateId: String, // exampletemplateid
    @Json(name = "users")
    val users: List<User>,
    @Json(name = "__v")
    val v: Int, // 0
    @Json(name = "workspace")
    val workspace: String // Global
)