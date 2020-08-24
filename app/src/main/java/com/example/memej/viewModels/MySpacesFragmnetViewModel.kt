package com.example.memej.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.repositories.MyWorkspacesRepository
import com.example.memej.responses.workspaces.UserWorkspaces
import retrofit2.Call
import retrofit2.Response

class MySpacesFragmnetViewModel constructor(private val repo: MyWorkspacesRepository) :
    ViewModel() {


    val TAG = MySpacesFragmnetViewModel::class.java.simpleName


    //For exiting the workspace
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    var message: MutableLiveData<String> = MutableLiveData()

    private val context = ApplicationUtil.getContext()
    private val sessionManager = SessionManager(context)
    private val workspaceService = RetrofitClient.callWorkspaces(context)

    lateinit var mySpacesResponse: UserWorkspaces

    //No body
    fun getMySpaces() {


        workspaceService.getUserSpaces(
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}"
        ).enqueue(object : retrofit2.Callback<UserWorkspaces> {
            override fun onFailure(call: Call<UserWorkspaces>, t: Throwable) {
                successful.value = false
                message.value = ErrorStatesResponse.returnStateMessageForThrowable(t)
            }

            override fun onResponse(
                call: Call<UserWorkspaces>,
                response: Response<UserWorkspaces>
            ) {

                if (response.isSuccessful) {
                    successful.value = true
                    message.value = response.message()
                    mySpacesResponse = response.body()!!

                }


            }
        })

    }


}
