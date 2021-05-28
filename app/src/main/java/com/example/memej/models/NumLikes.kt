package com.example.memej.models


import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class NumLikes(
    @field:Json(name = "likes")
    val likes: Int // 4
)