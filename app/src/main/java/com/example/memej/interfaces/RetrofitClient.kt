package com.example.memej.interfaces

import android.content.Context
import com.example.memej.Utils.DiffUtils.AuthInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {




    val url = "https://memej.herokuapp.com/"


    //OkHttp Client
    //Create OkHttp Client as well
//    private val okHttpClient = OkHttpClient.Builder()
//        .addInterceptor { chain ->
//            val original = chain.request()
//
//            //Header value
//            val requestBuilder = original.newBuilder()
//                .addHeader("Auhorization", AUTH)
//                .method(original.method, original.body)
//
//            val request = requestBuilder.build()
//            chain.proceed(request)
//        }.build()






//    //Use OkHttp Client
//    val instanceAuth: Auth by lazy {
//        //Return retrofit builders
//        val retrofit = Retrofit.Builder()
//            .baseUrl(url)
//            .addConverterFactory(MoshiConverterFactory.create())
//            .addCallAdapterFactory(CoroutineCallAdapterFactory())
//            .client(client)
//            .build()
//        retrofit.create(Auth::class.java)
//
//    }

    fun getAuthInstance(): Auth {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(url)
            .build()
            .create(Auth::class.java)
    }


    //Test Api for memes
    //Create a Interceptor Auth
    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()
    }


    fun makeCallsForMemes(context: Context): memes {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okhttpClient(context))
            .build().create(memes::class.java)

    }

    fun profileCalls(): Profile {
        return Retrofit.Builder()
            .baseUrl("https://api.jsonbin.io/b/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(Profile::class.java)
    }

}