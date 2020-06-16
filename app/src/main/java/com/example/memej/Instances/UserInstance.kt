package com.example.memej.Instances

import android.util.Log
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.ProfileResponse
import com.example.memej.responses.memeWorldResponses.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun UserInstance(): User {

    val ctx = ApplicationUtil.getContext()
    val apiservice = RetrofitClient.getAuthInstance()
    val sessionManager =
        SessionManager(ctx)


    var username: String? = ""
    var userId: String? = ""

    apiservice.getUser(accessToken = "Bearer ${sessionManager.fetchAcessToken()}")
        .enqueue(
            object : Callback<ProfileResponse> {
                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Log.e("MW", "Van not get profile, loaded default")

                }

                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    Log.e("MW", "Profile")


                    username = response.body()?.profile?._id.toString()
                    userId = response.body()?.profile?.username.toString()


                }
            })

    return User(userId.toString(), username.toString())
}

