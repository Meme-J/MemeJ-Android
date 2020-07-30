package com.example.memej.responses.workspaces


import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class CreateWorkspaceResponse(
    @field:Json(name = "msg")
    val msg: String // Workspace created successfully.
    //Or else an error
)