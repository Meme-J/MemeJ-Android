package com.example.memej.models.responses.workspaces


import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class ExitWorkspaceResponse(

    @field:Json(name = "msg")
    val msg: String         //Workspace exit status

)