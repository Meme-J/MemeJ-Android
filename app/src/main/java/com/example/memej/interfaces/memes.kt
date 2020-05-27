package com.example.memej.interfaces

import com.example.memej.entities.searchBody
import com.example.memej.responses.LikeOrNotResponse
import com.example.memej.responses.SearchResponse
import com.example.memej.responses.homeMememResponses.homeMemeApiResponse
import com.example.memej.responses.memeGroupApiResponse
import com.example.memej.responses.memeTemplateApiResponse
import com.example.memej.responses.memeWorldResponses.memeApiResponses
import retrofit2.Call
import retrofit2.http.*

interface memes {
//To use pagination, we need to create data sources
//Mostly used : page keyed
//To search by tags : Position Keyed


    //Get the meme group images with tags
    @GET("5ebaaff68284f36af7b9e764/2")
    suspend fun fetchMemeGroups(
        @Query("limit") loadSize: Int = 30,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null
    ): retrofit2.Response<memeGroupApiResponse>


    //Get the memes of a particular tag
    @GET("")
    suspend fun fetchMemeTemplates(
        @Query("limit") loadSize: Int = 1,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
        @Query("memeGroupId") memeGroupId: Int? = 0                      //This query will be sent
    ): retrofit2.Response<memeTemplateApiResponse>

    //Get memes of home


    //Memes of Home
    @POST("api/meme/ongoing")
    fun fetchEditableMemes(
        @Query("limit") loadSize: Int = 30,
        @Header("Authorization") accessToken: String?
    ): Call<homeMemeApiResponse>

    //Get search suggestions
    @POST("api/meme/autocomplete")
    fun getSuggestions(
        @Header("Authorization") accessToken: String?
        , @Body info: searchBody
    ): Call<SearchResponse>


    //Get the memes of the memeWorld
    @POST("api/meme/complete")
    fun fetchMemeWorldMemes(
        @Query("limit") loadSize: Int = 30,              //Test it with this value
        @Header("Authorization") accessToken: String?
    ): Call<memeApiResponses>


    //Like or dislike a meme
    @Headers("Content-Type:application/json")
    @POST("api/meme/like")
    fun likeMeme(
        @Body memeId: String,
        @Header("Authorization") accessToken: String?
    ): Call<LikeOrNotResponse>


}