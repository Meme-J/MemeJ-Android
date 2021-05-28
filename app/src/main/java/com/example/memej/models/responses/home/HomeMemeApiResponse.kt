package com.example.memej.models.responses.home

import androidx.annotation.Keep

@Keep
class HomeMemeApiResponse(val lastMemeId: String, val memes: List<Meme_Home>)