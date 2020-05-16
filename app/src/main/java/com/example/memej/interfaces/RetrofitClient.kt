package com.example.memej.interfaces

import android.util.Base64
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {


    //Login Credentials to create test client
    private val AUTH =
        "Basic" + Base64.encodeToString("Kavya24:123456".toByteArray(), Base64.NO_WRAP)

    const val BASE_URL = "https://www.json-generator.com/api/json/get/"

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


    //Client 2
    val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    val client: OkHttpClient = OkHttpClient.Builder().apply {
        this.addInterceptor(interceptor)
    }.build()


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
    fun makeCallsForMemes(): memes {
        return Retrofit.Builder()
            .baseUrl("https://api.jsonbin.io/b/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(memes::class.java)

    }


}