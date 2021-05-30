package com.example.memej.models.body.workspaces


import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class RejectWorkspaceRequestBody(
    @Json(name = "request")
    val request: Request
) {
    @Keep
    data class Request(
        @Json(name = "from")
        val from: String, // KavyaVmatsal
        @Json(name = "workspaceId")
        val workspaceId: String, // 5ebc2313fbe833d78dc971
        @Json(name = "workspaceName")
        val workspaceName: String // secongworkspacee
    )
}