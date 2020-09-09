package com.example.memej.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memej.R
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.homeMememResponses.homeMemeApiResponse
import retrofit2.Call
import retrofit2.Response

class ExploreViewModel : ViewModel() {

    val TAG = ExploreViewModel::class.java.simpleName


    //For exiting the workspace
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    var message: MutableLiveData<String> = MutableLiveData()

    private val context = ApplicationUtil.getContext()
    private val sessionManager = SessionManager(context)
    private val memeService = RetrofitClient.makeCallsForMemes(context)


    private var exploreResponse: MutableLiveData<homeMemeApiResponse> = MutableLiveData()

    fun randomFunction(): MutableLiveData<homeMemeApiResponse> {
        //If mySpacesResponses is null
        exploreResponse = getRandom()
        return exploreResponse


    }

    //No body
    fun getRandom(): MutableLiveData<homeMemeApiResponse> {

        Log.e(TAG, "In  egt spaces VM")

        memeService.getRandom(
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}"
        ).enqueue(object : retrofit2.Callback<homeMemeApiResponse> {
            override fun onFailure(call: Call<homeMemeApiResponse>, t: Throwable) {
                successful.value = false
                message.value = ErrorStatesResponse.returnStateMessageForThrowable(t)
                Log.e(TAG, "In failure message is ${message.value}")

            }

            override fun onResponse(
                call: Call<homeMemeApiResponse>,
                response: Response<homeMemeApiResponse>
            ) {

                Log.e(
                    TAG,
                    "In response and values are" + response.message() + "\n" + response.body()?.memes.toString()
                )


                if (response.isSuccessful) {
                    successful.value = true
                    message.value = context.getString(R.string.exploreSuccess)
                    exploreResponse.value = response.body()!!

                } else {
                    successful.value = false
                    message.value = context.getString(R.string.fail_explore)
                }


            }
        })


        return exploreResponse
    }


}
