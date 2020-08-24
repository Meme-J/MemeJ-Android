package com.example.memej.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.body.searchBody
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.SearchResponse
import com.example.memej.responses.workspaces.UserWorkspaces
import retrofit2.Call
import retrofit2.Response

class MainActivityViewModel : ViewModel() {

    var tag = LoginActivtyViewModel::class.java.simpleName
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""


    var suggestionList: MutableLiveData<String> = MutableLiveData()

    val context = ApplicationUtil.getContext()
    private val sessionManager: SessionManager = SessionManager(context)
    val memeService = RetrofitClient.makeCallsForMemes(context)
    val workspaceserbice = RetrofitClient.callWorkspaces(context)


    var workspaceResponses: UserWorkspaces? = null
    val successfulSpaces: MutableLiveData<Boolean> = MutableLiveData()
    val messageSpace: MutableLiveData<String> = MutableLiveData()


    fun fetchSuggestions(body: searchBody): MutableLiveData<String> {

        memeService.getSuggestions(
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}",
            info = body
        ).enqueue(object : retrofit2.Callback<SearchResponse> {
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                message = ErrorStatesResponse.returnStateMessageForThrowable(t)
                successful.value = false

            }

            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {

                if (response.isSuccessful) {
                    successful.value = true
                    //Response body is the suggestions list
                    val str = mutableListOf<String>()
                    for (y: SearchResponse.Suggestion in response.body()!!.suggestions) {
                        str.add(y.tag)
                    }


                }


            }
        })

        return suggestionList
    }

    fun getSpacesInDialog(): UserWorkspaces? {
        workspaceserbice.getUserSpaces(
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
                    messageSpace.value = response.message()
                    workspaceResponses = response.body()
                } else {
                    successfulSpaces.value = false
                    messageSpace.value = response.errorBody().toString()
                }
            }
        })


        return workspaceResponses
    }


}