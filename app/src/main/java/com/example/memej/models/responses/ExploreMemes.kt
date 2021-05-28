package com.example.memej.models.responses


import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
class ExploreMemes : ArrayList<ExploreMemes.ExploreMemesItem>() {
    data class ExploreMemesItem(
        @field:Json(name = "_id")
        val id: String, // 5ebb18b21f22b62ebb8dd2e7
        @field:Json(name = "lastUpdated")
        val lastUpdated: String, // 2020-05-12T21:44:18.360Z
        @field:Json(name = "numPlaceholders")
        val numPlaceholders: Int, // 3
        @field:Json(name = "placeholders")
        val placeholders: List<String>,
        @field:Json(name = "stage")
        val stage: Int, // 2
        @field:Json(name = "tags")
        val tags: List<Any>,
        @field:Json(name = "templateId")
        val templateId: String, // exampletemplateid
        @field:Json(name = "users")
        val users: List<User>,
        @field:Json(name = "__v")
        val v: Int, // 0
        @field:Json(name = "workspace")
        val workspace: String // Global
    ) {
        data class User(
            @field:Json(name = "_id")
            val id: String, // 5ebb18141f22b62ebb8dd2e4
            @field:Json(name = "username")
            val username: String // KavyaVmatsal
        )
    }
}