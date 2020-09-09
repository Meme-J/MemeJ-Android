package com.example.memej.responses.workspaces


import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class SendRequestsWorkspaceResponse(
    @Json(name = "msg")
    val msg: String // Requests sent successfully.
)