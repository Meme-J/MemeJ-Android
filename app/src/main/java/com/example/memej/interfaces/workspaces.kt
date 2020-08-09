package com.example.memej.interfaces

import com.example.memej.responses.workspaces.CreateWorkspaceBody
import com.example.memej.responses.workspaces.CreateWorkspaceResponse
import com.example.memej.responses.workspaces.UserWorkspaces
import com.example.memej.responses.workspaces.WorkspaceName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface workspaces {

    //Check if the name exists or not
    //# Edit this
    @POST("api/workspace/getname")
    @Headers("Content-Type:application/json")
    fun checkWorkspaceName(
        @Header("Authorization") accessToken: String?,
        @Body string: String
    ): Call<WorkspaceName>


    //Get the user workspaces
    @POST("api/user/workspaces")
    @Headers("Content-Type:application/json")
    fun getUserSpaces(
        @Header("Authorization") accessToken: String?
    ): Call<UserWorkspaces>

    //Create workspace
    @POST("api/user/workspaces")
    @Headers("Content-Type:application/json")
    fun createSpaces(
        @Header("Authorization") accessToken: String?,
        @Body body: CreateWorkspaceBody
    ): Call<CreateWorkspaceResponse>


}