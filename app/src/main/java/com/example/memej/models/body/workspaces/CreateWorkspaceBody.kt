package com.example.memej.models.body.workspaces

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class CreateWorkspaceBody(
    @field:Json(name = "name")
    val name: String

//    @field:Json(name = "tags")
//    val tags: List<String> = listOf()


)