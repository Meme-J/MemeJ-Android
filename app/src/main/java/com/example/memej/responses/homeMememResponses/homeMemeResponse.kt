package com.example.memej.responses.homeMememResponses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class homeMemeResponse(
    @Json(name = "memes")
    val memes: Meme_Home
)

