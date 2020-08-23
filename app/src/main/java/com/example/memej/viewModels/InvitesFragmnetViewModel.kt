package com.example.memej.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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


    var invitesResponse: UserRequestResponse? = null


    fun loadInvites(): UserRequestResponse? {

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

                if (response.isSuccessful) {
                    successful.value = true
                    message.value = response.message()
                    invitesResponse = response.body()

                } else {
                    successful.value = false
                    message.value = response.errorBody().toString()
                }


            }
        })

        return invitesResponse


    }


}
