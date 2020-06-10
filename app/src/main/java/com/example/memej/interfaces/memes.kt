package com.example.memej.interfaces

import com.example.memej.entities.editMemeBody
import com.example.memej.entities.queryBody
import com.example.memej.entities.searchBody
import com.example.memej.responses.LikeOrNotResponse
import com.example.memej.responses.SearchResponse
import com.example.memej.responses.editMemeApiResponse
import com.example.memej.responses.homeMememResponses.homeMemeApiResponse
import com.example.memej.responses.memeWorldResponses.SuggestionsResponse
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
    fun fetchEditableMemes(
        @Query("limit") loadSize: Int = 30,
        @Header("Authorization") accessToken: String?,
        @Body tags: queryBody                 //Default for normal calling, but a separate string incase a new param called
    ): Call<homeMemeApiResponse>


    //Get search suggestions of memes
    @POST("api/meme/autocomplete")
    fun getSuggestions(
        @Header("Authorization") accessToken: String?
        , @Body info: searchBody
    ): Call<SearchResponse>


    //Get the memes of the memeWorld
    @POST("api/meme/complete")
    fun fetchMemeWorldMemes(
        @Query("limit") loadSize: Int = 30,              //Test it with this value
        @Header("Authorization") accessToken: String?,
        @Body tag: queryBody
    ): Call<memeApiResponses>


    //Like or dislike a meme
    @Headers("Content-Type:application/json")
    @POST("api/meme/like")
    fun likeMeme(
        @Body memeId: String,
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
        @Query("limit") loadSize: Int = 30,              //Test it with this value
        @Header("Authorization") accessToken: String?
    ): Call<EmptyTemplateResponse>

    //To open an empty meme template
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

    //## memes by tags
    @POST("api/meme/tags")
    fun getMemeByTag(
        @Query("limit") loadSize: Int = 30,
        @Header("Authorization") accessToken: String?,
        @Body info: SuggestionsResponse         //This will be a string tag to be sent to the server
    ): Call<homeMemeApiResponse>

    //Edit memes response
    @POST("api/meme/edit")
    fun editMeme(
        @Header("Authorization") accessToken: String?,
        @Body info: editMemeBody
    ): Call<editMemeApiResponse>

    //## get my memes ( I have contributed into)
    //Response will be moxed of meme world, and ongoing
    //But It will come mixed
    @POST("api/meme/myMemes")
    fun getMyMemes(
        @Query("limit") loadSize: Int = 30,
        @Header("Authorization") accessToken: String?
    ): Call<homeMemeApiResponse>


}