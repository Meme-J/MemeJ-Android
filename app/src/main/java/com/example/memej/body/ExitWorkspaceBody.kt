package com.example.memej.body


import com.squareup.moshi.Json

data class ExitWorkspaceBody(
    @Json(name = "workspace")
    val workspace: Workspace
) {
    data class Workspace(
        @Json(name = "_id")
        val _id: String, // 5ebc29b313fbe833d78dc971
        @Json(name = "name")
        val name: String // secongworkspace
    )
}