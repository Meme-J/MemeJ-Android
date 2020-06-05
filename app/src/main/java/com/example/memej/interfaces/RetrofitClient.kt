package com.example.memej.interfaces

import android.content.Context
import android.util.Log
import com.example.memej.Utils.DiffUtils.AuthInterceptor
import com.example.memej.Utils.TokenAuthenticator
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {


    val url = "https://memej.herokuapp.com/"

    //Test Api for memes
    //Create a Interceptor Auth
    private fun okhttpClient(context: Context): OkHttpClient {
        //Add Authenticator

        Log.e("Retrofit Client", " In ok http")
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .authenticator(TokenAuthenticator(context))
            .build()
    }


    fun getAuthInstance(): Auth {
        Log.e("Retrofit Client", " Auth ")

        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(Auth::class.java)
    }



    fun makeCallsForMemes(context: Context): memes {
        Log.e("Retrofit Client", " In memes client")

        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okhttpClient(context))
            .build().create(memes::class.java)

    }

    fun makeCallForProfileParameters(context: Context): profile {
        Log.e("Retrofit Client", "prof")

        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okhttpClient(context))
            .build().create(profile::class.java)
    }


}