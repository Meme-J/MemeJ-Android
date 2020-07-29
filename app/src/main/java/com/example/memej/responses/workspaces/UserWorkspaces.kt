package com.example.memej.responses.workspaces


import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
//Class to show the workspaces a user is part of
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