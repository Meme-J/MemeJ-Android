package com.example.memej.Utils.interceptors

import android.content.Context
import android.util.Log
import com.example.memej.Utils.sessionManagers.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor to add auth token to requests access tokens
 */

class AuthInterceptor(context: Context) : Interceptor {

    private val sessionManager =
        SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {

        val requestBuilder = chain.request().newBuilder()

        // If token has been saved, add it to the request
        sessionManager.fetchAcessToken()?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        Log.e("auth", "In auth Intercptor")

        return chain.proceed(requestBuilder.build())
    }
}

