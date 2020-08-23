package com.example.memej.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.body.ExitWorkspaceBody
import com.example.memej.body.GenerateLinkBody
import com.example.memej.body.SendWorkspaceRequestBody
import com.example.memej.body.searchUserBody
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.SearchUserResponses
import com.example.memej.responses.workspaces.ExitWorkspaceResponse
import com.example.memej.responses.workspaces.GenerateLinkResponse
import com.example.memej.responses.workspaces.SendRequestsWorkspaceResponse
import retrofit2.Call
import retrofit2.Response

class WorkspaceViewModel : ViewModel() {

    val TAG = WorkspaceViewModel::class.java.simpleName

    //For exiting the workspace
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""

    //For generating the link
    var generateLinkBool: MutableLiveData<Boolean> = MutableLiveData()
    var messageLink: MutableLiveData<String> = MutableLiveData()


    //For search users by useranme to get added in workspace function
    var inviteBool: MutableLiveData<Boolean> = MutableLiveData()
    var messageInvite: MutableLiveData<String> = MutableLiveData()


    //For inviting users
    var sendBool: MutableLiveData<Boolean> = MutableLiveData()
    var messageSend: MutableLiveData<String> = MutableLiveData()

    private val context = ApplicationUtil.getContext()
    private val sessionManager = SessionManager(context)
    private val workspaceService = RetrofitClient.callWorkspaces(context)

    var exitResponse: ExitWorkspaceResponse? = null
    var generateLinkResponse: MutableLiveData<GenerateLinkResponse> = MutableLiveData()
    var getSearchedUsersResponse: SearchUserResponses? = null
    var inviteUsersPesponse: SendRequestsWorkspaceResponse? = null

    fun exitSpace(body: ExitWorkspaceBody.Workspace): ExitWorkspaceResponse? {


        workspaceService.exitSpaces(
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}",
            body = body
        )
            .enqueue(object : retrofit2.Callback<ExitWorkspaceResponse> {
                override fun onFailure(call: Call<ExitWorkspaceResponse>, t: Throwable) {
                    successful.value = false
                    message = ErrorStatesResponse.returnStateMessageForThrowable(t)

                }

                override fun onResponse(
                    call: Call<ExitWorkspaceResponse>,
                    response: Response<ExitWorkspaceResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.msg == "Workspace exited successfully.") {
                            successful.value = true
                            message = "Exit from the workspace successful"
                            exitResponse = response.body()!!

                        } else {
                            successful.value = false
                            message = response.body()!!.msg

                        }
                    }

                }
            })


        return exitResponse


    }

    fun generateLink(body: GenerateLinkBody): MutableLiveData<GenerateLinkResponse>? {

        workspaceService.generateLink(
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}",
            body = body
        )
            .enqueue(object : retrofit2.Callback<GenerateLinkResponse> {
                override fun onFailure(call: Call<GenerateLinkResponse>, t: Throwable) {
                    generateLinkBool.value = false
                    messageLink.value = ErrorStatesResponse.returnStateMessageForThrowable(t)

                }

                override fun onResponse(
                    call: Call<GenerateLinkResponse>,
                    response: Response<GenerateLinkResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.msg == "link created successfully.") {
                            generateLinkBool.value = true
                            messageLink.value = "Link created successfully"
                            generateLinkResponse.value = response.body()!!

                        } else {
                            generateLinkBool.value = false
                            messageLink.value = response.body()?.msg.toString()
                        }

                    } else {
                        generateLinkBool.value = false
                        messageLink.value = response.errorBody().toString()
                    }

                }
            })


        return generateLinkResponse


    }

    fun searchUsers(s: String): SearchUserResponses? {

        val body = searchUserBody(s)
        workspaceService.searchUsers(
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}",
            body = body
        ).enqueue(object : retrofit2.Callback<SearchUserResponses> {
            override fun onFailure(call: Call<SearchUserResponses>, t: Throwable) {
                inviteBool.value = false
                messageInvite.value = ErrorStatesResponse.returnStateMessageForThrowable(t)
            }

            override fun onResponse(
                call: Call<SearchUserResponses>,
                response: Response<SearchUserResponses>
            ) {
                if (response.isSuccessful) {
                    //When we get a suggestions response
                    inviteBool.value = true
                    messageInvite.value = response.message()
                    getSearchedUsersResponse = response.body()!!
                }

            }
        })

        return getSearchedUsersResponse

    }

    fun inviteUsers(body: SendWorkspaceRequestBody): SendRequestsWorkspaceResponse? {

        workspaceService.inviteUsers(
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}",
            body = body
        ).enqueue(object : retrofit2.Callback<SendRequestsWorkspaceResponse> {
            override fun onFailure(call: Call<SendRequestsWorkspaceResponse>, t: Throwable) {
                sendBool.value = false
                messageSend.value = ErrorStatesResponse.returnStateMessageForThrowable(t)
            }

            override fun onResponse(
                call: Call<SendRequestsWorkspaceResponse>,
                response: Response<SendRequestsWorkspaceResponse>
            ) {
                if (response.isSuccessful) {

                    sendBool.value = true
                    messageSend.value = response.message()
                    inviteUsersPesponse = response.body()!!
                }

            }
        })

        return inviteUsersPesponse
    }

}