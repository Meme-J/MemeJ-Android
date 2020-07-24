package com.example.memej.responses.workspaces


import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class UserWorkspaces(

    @field:Json(name = "workspaces")
    val workspaces: List<Workspace>
) {
    data class Workspace(
        @field:Json(name = "_id")
        val _id: String, // u
        @field:Json(name = "name")
        val name: String // secongworkspacee
    )
}