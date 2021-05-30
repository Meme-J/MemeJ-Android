package com.example.memej.models.responses.meme_world

import androidx.annotation.Keep

@Keep
data class MemeWorldApiResponses(
    val lastMemeId: String,
    val memes: List<Meme_World>
)