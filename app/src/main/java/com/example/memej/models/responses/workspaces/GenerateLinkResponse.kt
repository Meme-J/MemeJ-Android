package com.example.memej.models.responses.workspaces


import android.annotation.SuppressLint
import androidx.annotation.Keep
import com.squareup.moshi.Json

@SuppressLint("ParcelCreator")
@Keep

data class GenerateLinkResponse(

    @field:Json(name = "link")
    val link: String,
    @field:Json(name = "msg")
    val msg: String
)

/**
 * Separate Error body
 */
