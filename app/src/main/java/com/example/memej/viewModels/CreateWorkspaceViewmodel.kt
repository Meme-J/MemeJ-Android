package com.example.memej.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.models.body.workspaces.CreateWorkspaceBody
import com.example.memej.models.responses.workspaces.CreateWorkspaceResponse
import retrofit2.Call
import retrofit2.Response

class CreateWorkspaceViewmodel : ViewModel() {

    val TAG = CreateWorkspaceViewmodel::class.java.simpleName

    //For checking if the name exists
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""

    //Booleans for creating the Space
    val successfulCreate: MutableLiveData<Boolean> = MutableLiveData()
    var messageCreate: MutableLiveData<String> = MutableLiveData()

    private var createSpaceResponse: MutableLiveData<CreateWorkspaceResponse> = MutableLiveData()

    var responseForCheck = false

    private val context = ApplicationUtil.getContext()
    val workspaceService = RetrofitClient.callWorkspaces(context)
    private val sessionManager: SessionManager = SessionManager(context)


    fun createFunction(
        name: String,
        tags: MutableList<String>
    ): MutableLiveData<CreateWorkspaceResponse> {
        createSpaceResponse = createSpace(name, tags)
        return createSpaceResponse
    }

    fun createSpace(
        name: String,
        tags: MutableList<String>
    ): MutableLiveData<CreateWorkspaceResponse> {

        val body = CreateWorkspaceBody(name)

        workspaceService.createSpaces(
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}",
            body = body
        ).enqueue(object : retrofit2.Callback<CreateWorkspaceResponse> {
            override fun onFailure(call: Call<CreateWorkspaceResponse>, t: Throwable) {

                messageCreate.value = ErrorStatesResponse.returnStateMessageForThrowable(t)
                successfulCreate.value = false

            }

            override fun onResponse(
                call: Call<CreateWorkspaceResponse>,
                response: Response<CreateWorkspaceResponse>
            ) {

                Log.e(
                    TAG,
                    response.body()?.msg + response.toString() + response.errorBody().toString()
                )
                if (response.isSuccessful) {
                    if (response.body()?.msg == "Workspace created successfully.") {
                        successfulCreate.value = true
                        messageCreate.value = "Workspace created successfully."
                    } else {
                        successfulCreate.value = false
                        messageCreate.value = response.body()!!.msg.toString()

                    }
                    createSpaceResponse.value = response.body()

                } else {
                    successfulCreate.value = false
                    messageCreate.value = response.errorBody()!!.toString()

                }
            }
        })

        return createSpaceResponse
    }


}