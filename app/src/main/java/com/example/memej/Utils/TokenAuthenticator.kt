package com.example.memej.Utils

import android.content.Context
import android.util.Log
import com.example.memej.interfaces.RetrofitClient
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException

class TokenAuthenticator(val context: Context) : Authenticator {

    val service = RetrofitClient.getAuthInstance()
    val sessionManager = SessionManager(context)

    @Throws(IOException::class)
    override fun authenticate(route: Route?, response: Response): Request? {
        // Refresh your access_token using a synchronous api request
        val newAccessToken =
            service.getAccessToken(refreshToken = sessionManager.fetchRefreshToken().toString())


        val resp = response.request.newBuilder()
            .header("Authorization", newAccessToken.toString())
            .build()

        Log.e("AT", resp.toString() + " resp")
        Log.e("AT", newAccessToken.toString() + " new accexs token value")
        return resp
    }

    //Store this token as the new vaalues of the refresh and access tokens


    @Throws(IOException::class)
    fun authenticatRoute(route: Route?, response: Response?): Request? {
        // Null indicates no attempt to authenticate.
        return null
    }


}