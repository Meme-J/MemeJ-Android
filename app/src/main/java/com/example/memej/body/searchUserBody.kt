package com.example.memej.body


import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class searchUserBody(
    @Json(name = "search")
    val search: String // kavya
)