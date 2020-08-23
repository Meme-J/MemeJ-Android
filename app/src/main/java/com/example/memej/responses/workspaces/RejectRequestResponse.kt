package com.example.memej.responses.workspaces


import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class RejectRequestResponse(
    @Json(name = "msg")
    val msg: String // Request rejected successfully.
)