package com.example.memej.Instances

import android.content.Context
import android.widget.Toast
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.ProfileResponse
import retrofit2.Call
import retrofit2.Response


class LoadUser {


    //Uses access token to get the user
    fun getUserDetails(context: Context) {

        val service = RetrofitClient.getAuthInstance()
        val sessionManager =
            SessionManager(context)
        service.getUser(accessToken = "Bearer ${sessionManager.fetchAcessToken()}")
            .enqueue(object : retrofit2.Callback<ProfileResponse> {
                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    if (response.isSuccessful) {

                    }
                }
            })


    }


}
