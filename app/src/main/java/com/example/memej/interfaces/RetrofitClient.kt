package com.example.memej.interfaces

import android.util.Base64
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {

    //Login Credentials to create test client
    private val AUTH =
        "Basic" + Base64.encodeToString("Kavya24:123456".toByteArray(), Base64.NO_WRAP)

    const val BASE_URL = "https://www.json-generator.com/api/json/get/"


    //OkHttp Client
    //Create OkHttp Client as well
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()

            val requestBuilder = original.newBuilder()
                .addHeader("Auhorization", AUTH)
                .method(original.method, original.body)

            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()


    //Use OkHttp Client
    val instanceAuth: Auth by lazy {
        //Return retrofit builders
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()
        retrofit.create(Auth::class.java)

    }

    //Test Api for memes
    fun makeCallsForMemes(): memes {
        return Retrofit.Builder()
            .baseUrl("https://api.jsonbin.io/b/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build().create(memes::class.java)

    }


}