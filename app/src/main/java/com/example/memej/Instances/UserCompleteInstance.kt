package com.example.memej.Instances

import android.content.Context
import android.util.Log
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.ProfileResponse
import retrofit2.Call
import retrofit2.Response

fun UserCompleteInstance(context: Context): ProfileResponse.Profile {


    val apiservice = RetrofitClient.getAuthInstance()
    val sessionManager =
        SessionManager(context)


    var username: String? = ""
    var userId: String? = ""
    var name: String? = ""
    var email: String? = ""

    apiservice.getUser(accessToken = "Bearer ${sessionManager.fetchAcessToken()}")
        .enqueue(
            object : retrofit2.Callback<ProfileResponse> {
                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Log.e("MW", "Van not get profile, loaded default")

                }

                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {

                    //Get the id, username

                    username = response.body()?.profile?._id.toString()
                    userId = response.body()?.profile?.username.toString()
                    name = response.body()?.profile?.name.toString()
                    email = response.body()?.profile?.email.toString()
                }
            })

    return ProfileResponse.Profile(
        userId.toString(),
        username.toString(),
        name.toString(),
        email.toString()
    )
}

