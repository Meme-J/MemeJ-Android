package com.example.memej.responses

import androidx.annotation.Keep

@Keep
data class editMemeApiResponse(val msg: String, val meme: Meme_Edit)