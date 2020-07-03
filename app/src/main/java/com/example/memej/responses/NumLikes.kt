package com.example.memej.responses


import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class NumLikes(
    @field:Json(name = "likes")
    val likes: Int // 4
)