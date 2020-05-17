package com.example.memej.interfaces

import com.example.memej.entities.LoginBody
import com.example.memej.entities.UserBody
import com.example.memej.responses.LoginResponse
import com.example.memej.responses.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface Auth {


    //Register
    //@FormUrlEncoded
//    @Headers("Content-Type:application/json")
//    @POST("api/user/signup")
//    fun createUser(
//        @Field("name") name: String,
//        @Field("username") username: String,
//        @Field("email") email: String,
//        @Field("password") pwd: String
//
//    ): Call<SignUpResponse>

    //SignUo User
    @Headers("Content-Type:application/json")
    @POST("api/user/signup")
    fun createUser(
        @Body info: UserBody
    ): Call<SignUpResponse>


    //Login
    @Headers("Content-Type:application/json")
    @POST("api/user/login")
    fun loginUser(
        @Body info: LoginBody
    ): Call<LoginResponse>


    //Logout the user
    @POST("")
    fun logout(): Call<Void?>?
}