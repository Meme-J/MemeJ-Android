package com.example.memej.responses.memeWorldResponses

import com.squareup.moshi.Json

data class User(
    @Json(name = "_id")
    val id: String, // 5ebb18141f22b62ebb8dd2e4
    @Json(name = "username")
    val username: String // KavyaVmatsal
)

