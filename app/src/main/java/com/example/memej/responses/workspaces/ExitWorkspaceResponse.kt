package com.example.memej.responses.workspaces


import com.squareup.moshi.Json

data class ExitWorkspaceResponse(

    @field:Json(name = "msg")
    val msg: String         //Workspace exit status

)