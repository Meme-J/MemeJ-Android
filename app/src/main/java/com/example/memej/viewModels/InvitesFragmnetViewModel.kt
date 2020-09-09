package com.example.memej.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memej.R
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.workspaces.UserRequestResponse
import retrofit2.Call
import retrofit2.Response

class InvitesFragmnetViewModel : ViewModel() {

    val TAG = InvitesFragmnetViewModel::class.java.simpleName

    //For exiting the workspace
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    var message: MutableLiveData<String> = MutableLiveData()


    private val context = ApplicationUtil.getContext()
    private val sessionManager = SessionManager(context)
    private val workspaceService = RetrofitClient.callWorkspaces(context)


    private var invitesResponse: MutableLiveData<UserRequestResponse> = MutableLiveData()

    fun loadFunction(): MutableLiveData<UserRequestResponse> {
        invitesResponse = loadInvites()
        return invitesResponse
    }

    fun loadInvites(): MutableLiveData<UserRequestResponse> {

        workspaceService.getRequests(
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}"
        ).enqueue(object : retrofit2.Callback<UserRequestResponse> {
            override fun onFailure(call: Call<UserRequestResponse>, t: Throwable) {
                successful.value = false
                message.value = ErrorStatesResponse.returnStateMessageForThrowable(t)
            }

            override fun onResponse(
                call: Call<UserRequestResponse>,
                response: Response<UserRequestResponse>
            ) {


                Log.e(
                    TAG,
                    "In the Invites Viewmodel and response is " + response.body()?.requests.toString() + " " + response.body()?.requests?.size + " and the" +
                            "response code is " + response.message()
                )
                if (response.isSuccessful) {
                    successful.value = true
                    message.value = response.message()
                    invitesResponse.value = response.body()

                } else {
                    successful.value = false
                    message.value = context.getString(R.string.getRequestsFailed)
                }


            }
        })

        return invitesResponse


    }


}
