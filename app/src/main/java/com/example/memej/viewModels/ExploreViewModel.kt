package com.example.memej.viewModels

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.memej.R
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.models.responses.home.HomeMemeApiResponse
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


    private var exploreResponse: MutableLiveData<HomeMemeApiResponse> = MutableLiveData()

    fun randomFunction(
        mSnackbarBody: SwipeRefreshLayout,
        pb: ProgressBar
    ): MutableLiveData<HomeMemeApiResponse> {
        //If mySpacesResponses is null
        exploreResponse = getRandom(mSnackbarBody, pb)
        return exploreResponse


    }

    private fun getRandom(
        mSnackbarBody: SwipeRefreshLayout,
        pb: ProgressBar
    ): MutableLiveData<HomeMemeApiResponse> {


        memeService.getRandom(
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}"
        ).enqueue(object : retrofit2.Callback<HomeMemeApiResponse> {
            override fun onFailure(call: Call<HomeMemeApiResponse>, t: Throwable) {
                successful.value = false
                message.value = ErrorStatesResponse.returnStateMessageForThrowable(t)

                ErrorStatesResponse.logThrowables(t, TAG)
                pb.visibility = View.GONE
                ErrorStatesResponse.createSnackbar(message.value, mSnackbarBody)
            }

            override fun onResponse(
                call: Call<HomeMemeApiResponse>,
                response: Response<HomeMemeApiResponse>
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
