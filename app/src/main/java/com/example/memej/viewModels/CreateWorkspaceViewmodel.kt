package com.example.memej.viewModels

import android.text.Editable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.workspaces.CreateWorkspaceBody
import com.example.memej.responses.workspaces.CreateWorkspaceResponse
import com.example.memej.responses.workspaces.WorkspaceName
import retrofit2.Call
import retrofit2.Response

class CreateWorkspaceViewmodel : ViewModel() {

    val TAG = CreateWorkspaceViewmodel::class.java.simpleName
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""

    val successfulCreate: MutableLiveData<Boolean> = MutableLiveData()
    var messageCreate: String = ""


    var responseForCheck = false

    private val context = ApplicationUtil.getContext()
    val workspaceService = RetrofitClient.callWorkspaces(context)
    private val sessionManager: SessionManager = SessionManager(context)

    fun checkSpace(editable: Editable): Boolean {

        //Check the state
        workspaceService.checkWorkspaceName(
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}", string = editable.toString()
        ).enqueue(object : retrofit2.Callback<WorkspaceName> {
            override fun onFailure(call: Call<WorkspaceName>, t: Throwable) {
                //Unable to get the name util
                val m = ErrorStatesResponse.returnStateMessageForThrowable(t)
                message = m
                successful.value = false
                Log.e(TAG, m)

            }

            override fun onResponse(call: Call<WorkspaceName>, response: Response<WorkspaceName>) {
                if (response.isSuccessful) {
                    successful.value = true
                    responseForCheck = response.body()!!.message == "Exists"


                }


            }
        })

        return responseForCheck

    }

    fun createSpace(name: String, tags: MutableList<String>): String {

        val body = CreateWorkspaceBody(name)

        workspaceService.createSpaces(
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}",
            body = body
        ).enqueue(object : retrofit2.Callback<CreateWorkspaceResponse> {
            override fun onFailure(call: Call<CreateWorkspaceResponse>, t: Throwable) {
                val y = ErrorStatesResponse.returnStateMessageForThrowable(t)
                messageCreate = y
                successfulCreate.value = false
            }

            override fun onResponse(
                call: Call<CreateWorkspaceResponse>,
                response: Response<CreateWorkspaceResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.msg == "Workspace created successfully.") {
                        successfulCreate.value = true
                        messageCreate = "Workspace created successfully."
                    } else {
                        successfulCreate.value = false
                        messageCreate = response.body()!!.msg.toString()

                    }
                } else {
                    successfulCreate.value = false
                    messageCreate = response.errorBody()!!.toString()

                }
            }
        })

        return messageCreate
    }


}