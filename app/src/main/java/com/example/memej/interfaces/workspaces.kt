package com.example.memej.interfaces

import com.example.memej.body.*
import com.example.memej.responses.SearchUserResponses
import com.example.memej.responses.workspaces.*
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
    @POST("/api/user/workspaces")
    fun getUserSpaces(
        @Header("Authorization") accessToken: String?
    ): Call<UserWorkspaces>


    //Create workspace
    @POST("api/workspace/create")
    @Headers("Content-Type:application/json")
    fun createSpaces(
        @Header("Authorization") accessToken: String?,
        @Body body: CreateWorkspaceBody
    ): Call<CreateWorkspaceResponse>


    //Exit the workspace
    @POST("api/workspace/exit")
    @Headers("Content-Type:application/json")
    fun exitSpaces(
        @Header("Authorization") accessToken: String?,
        @Body body: ExitWorkspaceBody
    ): Call<ExitWorkspaceResponse>


    //Generate Link for joining the space
    @POST("api/workspace/link")
    @Headers("Content-Type:application/json")
    fun generateLink(
        @Header("Authorization") accessToken: String?,
        @Body body: GenerateLinkBody
    ): Call<GenerateLinkResponse>


    //Call Users (Search by the usernames)
    @POST("api/user/autocomplete")
    @Headers("Content-Type:application/json")
    fun searchUsers(
        @Header("Authorization") accessToken: String?,
        @Body body: searchUserBody
    ): Call<SearchUserResponses>


    //Invite the users
    @POST("api/workspace/sendrequests")
    @Headers("Content-Type:application/json")
    fun inviteUsers(
        @Header("Authorization") accessToken: String?,
        @Body body: SendWorkspaceRequestBody
    ): Call<SendRequestsWorkspaceResponse>

    //Get the request received by the users by other people
    @POST("api/user/requests")
    @Headers("Content-Type:application/json")
    fun getRequests(
        @Header("Authorization") accessToken: String?
    ): Call<UserRequestResponse>


    //Accept a request from the workspace
    @POST("api/workspace/join")
    @Headers("Content-Type:application/json")
    fun acceptRequests(
        @Header("Authorization") accessToken: String?,
        @Body body: AcceptRequestsBody
    ): Call<AcceptRequestsResponse>

    //Reject a request
    @POST("api/workspace/reject")
    @Headers("Content-Type:application/json")
    fun rejectRequests(
        @Header("Authorization") accessToken: String?,
        @Body body: RejectRequestBody
    ): Call<RejectRequestResponse>


}