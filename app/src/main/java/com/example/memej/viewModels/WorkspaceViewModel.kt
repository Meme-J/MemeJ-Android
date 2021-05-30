package com.example.memej.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memej.R
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.models.body.search.SearchUserBody
import com.example.memej.models.body.workspaces.ExitWorkspaceBody
import com.example.memej.models.body.workspaces.GenerateLinkBody
import com.example.memej.models.body.workspaces.SendWorkspaceRequestBody
import com.example.memej.models.responses.search.SearchUserResponses
import com.example.memej.models.responses.workspaces.ExitWorkspaceResponse
import com.example.memej.models.responses.workspaces.GenerateLinkResponse
import com.example.memej.models.responses.workspaces.SendRequestsWorkspaceResponse
import retrofit2.Call
import retrofit2.Response

class WorkspaceViewModel : ViewModel() {

    val TAG = WorkspaceViewModel::class.java.simpleName

    //For exiting the workspace
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    var message: MutableLiveData<String> = MutableLiveData()

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

    private var exitResponse: MutableLiveData<ExitWorkspaceResponse> = MutableLiveData()
    var generateLinkResponse: MutableLiveData<GenerateLinkResponse> = MutableLiveData()
    var getSearchedUsersResponse: MutableLiveData<SearchUserResponses> = MutableLiveData()
    var inviteUsersPesponse: MutableLiveData<SendRequestsWorkspaceResponse> = MutableLiveData()


    fun exitFunction(body: ExitWorkspaceBody): MutableLiveData<ExitWorkspaceResponse> {
        exitResponse = exitSpace(body)
        return exitResponse

    }

    fun exitSpace(body: ExitWorkspaceBody): MutableLiveData<ExitWorkspaceResponse> {


        workspaceService.exitSpaces(
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}",
            body = body
        )
            .enqueue(object : retrofit2.Callback<ExitWorkspaceResponse> {
                override fun onFailure(call: Call<ExitWorkspaceResponse>, t: Throwable) {
                    successful.value = false
                    message.value = ErrorStatesResponse.returnStateMessageForThrowable(t)

                }

                override fun onResponse(
                    call: Call<ExitWorkspaceResponse>,
                    response: Response<ExitWorkspaceResponse>
                ) {

                    if (response.isSuccessful) {
                        exitResponse.value = response.body()
                        successful.value = true
                        Log.e(TAG, response.body().toString())
                    } else {
                        successful.value = false
                        message.value = response.body()?.msg!!
                    }
                }
            })


        return exitResponse


    }


    fun generateFunction(body: GenerateLinkBody): MutableLiveData<GenerateLinkResponse> {

        generateLinkResponse = generateLink(body)
        return generateLinkResponse
    }


    fun generateLink(body: GenerateLinkBody): MutableLiveData<GenerateLinkResponse> {

        workspaceService.generateLink(
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}",
            body = body
        )
            .enqueue(object : retrofit2.Callback<GenerateLinkResponse> {
                override fun onFailure(call: Call<GenerateLinkResponse>, t: Throwable) {
                    generateLinkBool.value = false
                    messageLink.value = ErrorStatesResponse.returnStateMessageForThrowable(t)
                    //Response is null
                }

                override fun onResponse(
                    call: Call<GenerateLinkResponse>,
                    response: Response<GenerateLinkResponse>
                ) {

                    Log.e(TAG, response.message() + response.body()?.msg + response.body()?.link)
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
                        messageLink.value = response.body()?.msg.toString()
                    }

                }
            })


        return generateLinkResponse


    }


    fun searchFunction(s: String): MutableLiveData<SearchUserResponses> {
        getSearchedUsersResponse = searchUsers(s)
        return getSearchedUsersResponse
    }


    fun searchUsers(s: String): MutableLiveData<SearchUserResponses> {

        val body = SearchUserBody(s)
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
                    getSearchedUsersResponse.value = response.body()!!
                } else {
                    inviteBool.value = false
                    messageInvite.value = context.getString(R.string.noUsers)
                }

            }
        })

        return getSearchedUsersResponse

    }

    fun inviteFunction(body: SendWorkspaceRequestBody): MutableLiveData<SendRequestsWorkspaceResponse> {
        inviteUsersPesponse = inviteUsers(body)
        return inviteUsersPesponse
    }

    private fun inviteUsers(body: SendWorkspaceRequestBody): MutableLiveData<SendRequestsWorkspaceResponse> {

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

                Log.e(TAG, response.body()?.msg + response.message() + response.errorBody())
                if (response.isSuccessful) {

                    sendBool.value = true
                    messageSend.value = response.body()?.msg
                    inviteUsersPesponse.value = response.body()!!
                } else {
                    sendBool.value = false
                    messageSend.value = response.body()?.msg
                }

            }
        })

        return inviteUsersPesponse
    }

}