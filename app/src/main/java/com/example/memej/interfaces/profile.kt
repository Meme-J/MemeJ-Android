package com.example.memej.interfaces

import androidx.annotation.Keep
import com.example.memej.body.profileSearchBody
import com.example.memej.responses.NumLikes
import com.example.memej.responses.UserProfileResponse
import com.example.memej.responses.memeWorldResponses.memeApiResponses
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query


@Keep
interface profile {

    //Get the number of total memes the person has created


    //Get the number of total likes
    @POST("api/user/likes")
    fun getNumLikesRecieved(
        @Header("Authorization") accessToken: String?
    ): Call<NumLikes>


    //Get the liked memes
    @POST("api/meme/likedmemes")
    fun getLikedMemes(
        @Query("limit") loadSize: Int = 30,              //Test it with this value
        @Header("Authorization") accessToken: String?
        //Response call is same as the meme world
    ): Call<memeApiResponses>


    //Get user is defined in auth

    //Get the profile of some user
    //##other user
    @POST("api/user/profile")
    fun getProfileFromUsername(
        @Body info: profileSearchBody
    ): Call<UserProfileResponse>


}