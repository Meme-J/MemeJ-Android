package com.example.memej.models.responses.edit

import androidx.annotation.Keep

@Keep
data class EditMemeApiResponse(val msg: String, val meme: Meme_Edit)