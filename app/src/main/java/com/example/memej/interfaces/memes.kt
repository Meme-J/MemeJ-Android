package com.example.memej.interfaces

import com.example.memej.entities.*
import com.example.memej.responses.LikeOrNotResponse
import com.example.memej.responses.SearchResponse
import com.example.memej.responses.editMemeApiResponse
import com.example.memej.responses.homeMememResponses.homeMemeApiResponse
import com.example.memej.responses.memeWorldResponses.memeApiResponses
import com.example.memej.responses.template.EmptyTemplateResponse
import retrofit2.Call
import retrofit2.http.*

interface memes {
//To use pagination, we need to create data sources
//Mostly used : page keyed
//To search by tags : Position Keyed


    //Memes of Home

    @POST("api/meme/ongoing")
    @Headers("Content-Type:application/json")
    fun fetchEditableMemes(
        @Query("limit") loadSize: Int = 20,
        @Header("Authorization") accessToken: String?,
        @Body search: queryBody                  //Default for normal calling, but a separate string incase a new param called
    ): Call<homeMemeApiResponse>


    //Get search suggestions of memes
    @Headers("Content-Type:application/json")
    @POST("api/meme/autocomplete")
    fun getSuggestions(
        @Header("Authorization") accessToken: String?
        , @Body info: searchBody
    ): Call<SearchResponse>


    //Get the memes of the memeWorld
    @POST("api/meme/complete")
    @Headers("Content-Type:application/json")
    fun fetchMemeWorldMemes(
        @Query("limit") loadSize: Int = 20,              //Test it with this value
        @Header("Authorization") accessToken: String?,
        @Body search: queryBody
    ): Call<memeApiResponses>


    //Like or dislike a meme
    @Headers("Content-Type:application/json")
    @POST("api/meme/like")
    fun likeMeme(
        @Body memeId: likeMemeBody,
        @Header("Authorization") accessToken: String?
    ): Call<LikeOrNotResponse>

    //Get a random meme
    @POST("api/meme/random")
    fun getRandom(
        @Header("Authorization") accessToken: String?
    ): Call<homeMemeApiResponse>              //Response will be similar form of home posts


    //Get an empty meme template
    @POST("api/template")
    fun getTemplate(
        @Query("limit") loadSize: Int = 20,              //Test it with this value
        @Header("Authorization") accessToken: String?
    ): Call<EmptyTemplateResponse>

    //To open an empty meme template
    //Not to be used as it fetches all the templates
    @POST("api/template/open")
    fun openTemplate(
        @Body inf: String,
        @Header("Authorization") accessToken: String?
    ): Call<EmptyTemplateResponse.Template>          //same as empty template response


    //Get the tags for memes
    @POST("api/meme/autocomplete")
    fun getTags(
        @Header("Authorization") accessToken: String?,
        @Body info: searchBody

    ): Call<SearchResponse>

    //Edit memes response
    @Headers("Content-Type:application/json")
    @POST("api/meme/edit")
    fun editMeme(
        @Header("Authorization") accessToken: String?,
        @Body info: editMemeBody
    ): Call<editMemeApiResponse>

    @Headers("Content-Type:application/json")
    @POST("api/meme/create")
    fun createMeme(
        @Header("Authorization") accessToken: String?,
        @Body info: createMemeBody
    ): Call<editMemeApiResponse>


    //## get my memes ( I have contributed into)
    //Response will be moxed of meme world, and ongoing
    //But It will come mixed
    @POST("api/meme/myMemes")
    fun getMyMemes(
        @Query("limit") loadSize: Int = 20,
        @Header("Authorization") accessToken: String?
    ): Call<homeMemeApiResponse>


}