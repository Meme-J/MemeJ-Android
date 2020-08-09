package com.example.memej.responses.workspaces

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
//Tells about wheather a name of workspace is already taken or not
data class WorkspaceName(

    @field:Json(name = "message")
    val message: String
)