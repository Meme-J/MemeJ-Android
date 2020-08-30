package com.example.memej.responses.workspaces


import androidx.annotation.Keep
import androidx.room.Entity
import com.squareup.moshi.Json

@Keep
//Class to show the workspaces a user is part of
@Entity
data class UserWorkspaces(

    @field:Json(name = "workspaces")
    val workspaces: List<Workspace>
) {
    @Keep
    @Entity
    data class Workspace(
        @field:Json(name = "_id")
        val _id: String,
        @field:Json(name = "name")
        val name: String
        //TODO:Num of loves, memes, people
    )
}