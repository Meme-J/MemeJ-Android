package com.example.memej.interfaces

import com.example.memej.responses.DefaultResponse
import com.example.memej.responses.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Auth {


    //Register
    @FormUrlEncoded
    @POST("createUser")
    fun createUser(
        @Field("name") name: String,
        @Field("userName") username: String,
        @Field("email") email: String,
        @Field("password") pwd: String

    ): Call<DefaultResponse>

    //Login User
    @FormUrlEncoded
    @POST("userLogin")
    fun userLogin(
        @Field("userName") username: String,
        @Field("password") pwd: String
    ): Call<LoginResponse>


}