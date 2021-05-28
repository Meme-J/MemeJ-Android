package com.example.memej.models.body.workspaces


import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class AcceptWorkspaceRequestBody(
    @Json(name = "request")
    val request: Request
) {
    @Keep
    data class Request(
        @Json(name = "from")
        val from: String, // kavya
        @Json(name = "_id")
        val id: String, // 5ebc29b313fbe833d78dc971
        @Json(name = "name")
        val name: String // secongworkspace
    )
}