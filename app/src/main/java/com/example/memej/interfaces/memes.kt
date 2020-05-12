package com.example.memej.interfaces

import com.example.memej.responses.memeGroupApiResponse
import com.example.memej.responses.memeTemplateApiResponse
import retrofit2.http.GET
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
        @Query("before") before: String? = null
    ): retrofit2.Response<memeTemplateApiResponse>
}