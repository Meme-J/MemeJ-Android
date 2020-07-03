package com.example.memej.responses.memeWorldResponses

import androidx.annotation.Keep

@Keep
data class memeApiResponses(
    val lastMemeId: String,
    val memes: List<Meme_World>
)