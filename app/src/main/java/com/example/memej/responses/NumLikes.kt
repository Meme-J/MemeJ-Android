package com.example.memej.responses


import com.squareup.moshi.Json


data class NumLikes(
    @field:Json(name = "likes")
    val likes: Int // 4
)