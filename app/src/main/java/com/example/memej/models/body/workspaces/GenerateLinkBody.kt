package com.example.memej.models.body.workspaces


import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class GenerateLinkBody(

    @field:Json(name = "workspace")
    val workspace: Workspace
) {

    @Keep
    data class Workspace(
        @field:Json(name = "_id")
        val _id: String,
        @field:Json(name = "name")
        val name: String
    )
}