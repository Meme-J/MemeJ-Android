package com.example.memej.interfaces

import com.example.memej.entities.LoginBody
import com.example.memej.entities.UserBody
import com.example.memej.responses.LoginResponse
import com.example.memej.responses.ProfileResponse
import com.example.memej.responses.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface Auth {


    //SignUp User
    @Headers("Content-Type:application/json")
    @POST("/api/user/signup")
    fun createUser(
        @Body info: UserBody
    ): Call<SignUpResponse>


    //Login
    @Headers("Content-Type:application/json")
    @POST("api/user/login")
    fun loginUser(
        @Body info: LoginBody
    ): Call<LoginResponse>


    @POST("api/user/profile")
    fun getUser(
        @Header("Authorization") accessToken: String?
    ): Call<ProfileResponse>


    @POST("api/user/accessToken")
    fun getAccessToken(
        //USe Refresh token here            //Passed as String
        @Body refreshToken: String
    ): Call<LoginResponse>


    //Logout the user
//    @POST("")
//    fun logout(): Call<Void?>?
}