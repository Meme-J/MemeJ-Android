package com.example.memej.interfaces

import androidx.annotation.Keep
import com.example.memej.models.body.auth.LoginBody
import com.example.memej.models.body.auth.SignUpBody
import com.example.memej.models.responses.auth.LoginResponse
import com.example.memej.models.responses.auth.ProfileResponse
import com.example.memej.models.responses.auth.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST


@Keep
interface auth {


    //SignUp User
    @Headers("Content-Type:application/json")
    @POST("/api/user/signup")
    fun createUser(
        @Body info: SignUpBody
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