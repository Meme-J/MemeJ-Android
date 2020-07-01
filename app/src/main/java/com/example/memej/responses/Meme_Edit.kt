package com.example.memej.responses


import com.squareup.moshi.Json

data class Meme_Edit(
    @field:Json(name = "_id")
    val _id: String, // 5ebb18b91f22b62ebb8dd2e8
    @field:Json(name = "lastUpdated")
    val lastUpdated: String, // 2020-05-12T21:44:25.083Z
    @field:Json(name = "numPlaceholders")
    val numPlaceholders: Int, // 3
    @field:Json(name = "placeholders")
    val placeholders: List<String>,
    @field:Json(name = "stage")
    val stage: Int, // 3
    @field:Json(name = "likes")
    val likes: Int, // 3
    @field:Json(name = "likedBy")
    val likedBy: List<User>, // 3
    @field:Json(name = "tags")
    val tags: List<String>,
    @field:Json(name = "templateId")
    val templateId: String, // exampletemplateid
    @field:Json(name = "users")
    val users: List<User>,
    @field:Json(name = "__v")
    val __v: Int, // 0
    @field:Json(name = "workspace")
    val workspace: String // Global
) {
    data class User(
        @field:Json(name = "_id")
        val _id: String, // 5ebb18141f22b62ebb8dd2e4
        @field:Json(name = "username")
        val username: String // KavyaVmatsal
    )
}