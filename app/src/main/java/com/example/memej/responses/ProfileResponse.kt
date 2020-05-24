package com.example.memej.responses


import com.squareup.moshi.Json

data class ProfileResponse(
    @Json(name = "email")
    val email: String, // kv@pol.com
    @Json(name = "_id")
    val id: String, // 5ebaa4879357ac2c929c764b
    @Json(name = "name")
    val name: String, // Kavya Goyal
    @Json(name = "username")
    val username: String // KavyaVmatsal
)