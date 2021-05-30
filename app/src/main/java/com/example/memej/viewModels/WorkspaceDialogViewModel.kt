package com.example.memej.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memej.R
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.models.responses.workspaces.UserWorkspaces
import retrofit2.Call
import retrofit2.Response

class WorkspaceDialogViewModel : ViewModel() {

    var tag = MainActivityViewModel::class.java.simpleName
    val context = ApplicationUtil.getContext()
    private val sessionManager: SessionManager = SessionManager(context)

    var workspaceResponses: MutableLiveData<UserWorkspaces> = MutableLiveData()
    val successfulSpaces: MutableLiveData<Boolean> = MutableLiveData()
    val messageSpace: MutableLiveData<String> = MutableLiveData()


    private val workspaceService = RetrofitClient.callWorkspaces(context)

    fun spacesFunction(): MutableLiveData<UserWorkspaces> {

        workspaceResponses = getSpacesInDialog()
        return workspaceResponses
    }


    private fun getSpacesInDialog(): MutableLiveData<UserWorkspaces> {

        workspaceService.getUserSpaces(
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}"
        ).enqueue(object : retrofit2.Callback<UserWorkspaces> {
            override fun onFailure(call: Call<UserWorkspaces>, t: Throwable) {
                successfulSpaces.value = false
                messageSpace.value = ErrorStatesResponse.returnStateMessageForThrowable(t)
            }

            override fun onResponse(
                call: Call<UserWorkspaces>,
                response: Response<UserWorkspaces>
            ) {
                if (response.isSuccessful) {
                    successfulSpaces.value = true
                    messageSpace.value = context.getString(R.string.spaceSucess)
                    workspaceResponses.value = response.body()
                } else {
                    successfulSpaces.value = false
                    messageSpace.value = context.getString(R.string.unableToLoadMySpaces)
                }
            }
        })


        return workspaceResponses
    }

}