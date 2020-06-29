package com.example.memej.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.entities.searchBody
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.SearchResponse
import retrofit2.Call
import retrofit2.Response

class MainActivityViewModel : ViewModel() {

    var tag = LoginActivtyViewModel::class.java.simpleName
    val context = ApplicationUtil.getContext()
    private val sessionManager: SessionManager = SessionManager(context)
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""
    val memeService = RetrofitClient.makeCallsForMemes(context)


    var suggestionList: MutableLiveData<String> = MutableLiveData()


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


}