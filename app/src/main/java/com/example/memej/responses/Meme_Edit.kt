package com.example.memej.responses


import com.squareup.moshi.Json

data class Meme_Edit(
    @Json(name = "_id")
    val id: String, // 5ebb18b91f22b62ebb8dd2e8
    @Json(name = "lastUpdated")
    val lastUpdated: String, // 2020-05-12T21:44:25.083Z
    @Json(name = "numPlaceholders")
    val numPlaceholders: Int, // 3
    @Json(name = "placeholders")
    val placeholders: List<String>,
    @Json(name = "stage")
    val stage: Int, // 3
    @Json(name = "tags")
    val tags: List<Any>,
    @Json(name = "templateId")
    val templateId: String, // exampletemplateid
    @Json(name = "users")
    val users: List<User>,
    @Json(name = "__v")
    val v: Int, // 0
    @Json(name = "workspace")
    val workspace: String // Global
) {
    data class User(
        @Json(name = "_id")
        val id: String, // 5ebb18141f22b62ebb8dd2e4
        @Json(name = "username")
        val username: String // KavyaVmatsal
    )
}