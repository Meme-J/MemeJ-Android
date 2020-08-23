package com.example.memej.responses.workspaces


import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class AcceptRequestsResponse(
    @Json(name = "msg")
    val msg: String // Join request successful.
)