package com.example.memej.responses.homeMememResponses


import com.squareup.moshi.Json

data class homeMemeResponse(
    @field:Json(name = "memes")
    val memes: Meme_Home
)

