package com.example.memej.body


import com.squareup.moshi.Json

data class GenerateLinkBody(
    @Json(name = "workspace")
    val workspace: Workspace
) {
    data class Workspace(
        @Json(name = "_id")
        val id: String,
        @Json(name = "name")
        val name: String
    )
}