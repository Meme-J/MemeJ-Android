package com.example.memej.interfaces

import com.example.memej.responses.ExploreMemes
import com.example.memej.responses.homeMememResponses.homeMemeApiResponse
import com.example.memej.responses.memeGroupApiResponse
import com.example.memej.responses.memeTemplateApiResponse
import com.example.memej.responses.memeWorldResponses.memeWorldResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

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


    @POST("api/meme/ongoing")
    fun fetchEditableMemes(
        @Query("limit") loadSize: Int = 30,
        @Header("Authorization") accessToken: String?
    ): Call<homeMemeApiResponse>


    suspend fun fetchExploreMemes(): Response<List<ExploreMemes>>


    //Get the memes of the memeWorld
    @GET("api/meme/completememes")
    suspend fun fetchMemeWorldMemes(
        @Query("limit") loadSize: Int = 5,              //Test it with this value
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
        @Query("lastMemeId") lastMemeId: String? = null

    ): Response<List<memeWorldResponse>>


}