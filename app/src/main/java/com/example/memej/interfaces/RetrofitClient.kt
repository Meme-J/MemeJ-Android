package com.example.memej.interfaces

import android.content.Context
import androidx.annotation.Keep
import com.example.memej.Utils.interceptors.AuthInterceptor
import com.example.memej.Utils.sessionManagers.TokenAuthenticator
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


@Keep
object RetrofitClient {


    val url = "https://memej.herokuapp.com/"
    val TAG = RetrofitClient::class.java.simpleName


    private fun okhttpClient(context: Context): OkHttpClient {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY


        //Enable Caching 5mb
        val cacheSize = (5 * 1024 * 1024).toLong()

        //Create cache
        val mCache = Cache(context.cacheDir, cacheSize)



        return OkHttpClient.Builder()
            // .addInterceptor(interceptor)
            .addInterceptor(
                AuthInterceptor(
                    context
                )
            )
            .followRedirects(false)
            .writeTimeout(20, TimeUnit.SECONDS)
            .authenticator(
                TokenAuthenticator(
                    context
                )
            )
            .build()
    }

    private fun authClient(): OkHttpClient {

        return OkHttpClient.Builder()
            .followRedirects(false)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    fun getAuthInstance(): auth {

        //Moshi class
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(authClient())
            .build()
            .create(auth::class.java)
    }


    fun makeCallsForMemes(context: Context): memes {


//        val moshi = Moshi.Builder()
//            .add(KotlinJsonAdapterFactory())
//            .build()
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okhttpClient(context))
            .build().create(memes::class.java)

    }

    fun makeCallForProfileParameters(context: Context): profile {


        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okhttpClient(context))
            .build().create(profile::class.java)
    }

    fun callWorkspaces(context: Context): workspaces {

        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okhttpClient(context))
            .build().create(workspaces::class.java)


    }


}