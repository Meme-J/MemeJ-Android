package com.example.memej.interfaces

import androidx.annotation.Keep
import com.example.memej.models.body.LikeMemeBody
import com.example.memej.models.body.create.CreateMemeBody
import com.example.memej.models.body.edit.EditMemeBody
import com.example.memej.models.body.search.QueryBody
import com.example.memej.models.body.search.SearchBody
import com.example.memej.models.body.search.SearchTemplateBody
import com.example.memej.models.responses.LikeOrNotResponse
import com.example.memej.models.responses.edit.EditMemeApiResponse
import com.example.memej.models.responses.home.HomeMemeApiResponse
import com.example.memej.models.responses.meme_world.MemeWorldApiResponses
import com.example.memej.models.responses.search.SearchResponse
import com.example.memej.models.responses.template.EmptyTemplateResponse
import retrofit2.Call
import retrofit2.http.*

@Keep
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
        @Body search: QueryBody                  //Default for normal calling, but a separate string incase a new param called
    ): Call<HomeMemeApiResponse>


    //Get search suggestions of memes
    @Headers("Content-Type:application/json")
    @POST("api/meme/autocomplete")
    fun getSuggestions(
        @Header("Authorization") accessToken: String?, @Body info: SearchBody
    ): Call<SearchResponse>


    //Get the memes of the memeWorld
    @POST("api/meme/complete")
    @Headers("Content-Type:application/json")
    fun fetchMemeWorldMemes(
        @Query("limit") loadSize: Int = 20,              //Test it with this value
        @Header("Authorization") accessToken: String?,
        @Body search: QueryBody
    ): Call<MemeWorldApiResponses>


    //Like or dislike a meme
    @Headers("Content-Type:application/json")
    @POST("api/meme/like")
    fun likeMeme(
        @Body memeId: LikeMemeBody,
        @Header("Authorization") accessToken: String?
    ): Call<LikeOrNotResponse>

    //Get a random meme
    @POST("api/meme/random")
    fun getRandom(
        @Header("Authorization") accessToken: String?
    ): Call<HomeMemeApiResponse>              //Response will be similar form of home posts


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
        @Body info: SearchBody

    ): Call<SearchResponse>

    //Edit memes response
    @Headers("Content-Type:application/json")
    @POST("api/meme/edit")
    fun editMeme(
        @Header("Authorization") accessToken: String?,
        @Body info: EditMemeBody
    ): Call<EditMemeApiResponse>

    @Headers("Content-Type:application/json")
    @POST("api/meme/create")
    fun createMeme(
        @Header("Authorization") accessToken: String?,
        @Body info: CreateMemeBody
    ): Call<EditMemeApiResponse>


    @Headers("Content-Type:application/json")
    @POST("api/template/search")
    fun getSearchedTemplates(
        @Query("limit") loadSize: Int = 20,              //Test it with this value
        @Header("Authorization") accessToken: String?,
        @Body info: SearchTemplateBody
    ): Call<EmptyTemplateResponse>


    //##This should be like getSuggestions of template search
    @Headers("Content-Type:application/json")
    @POST("api/template/autocomplete")
    fun getTemplateSuggestions(
        @Header("Authorization") accessToken: String?,
        @Body info: SearchTemplateBody
    ): Call<SearchResponse>


    //## get my memes ( I have contributed into)
    //Response will be moxed of meme world, and ongoing
    //But It will come mixed

    @POST("api/meme/mymemes")
    fun getMyMemes(
        @Query("limit") loadSize: Int = 20,
        @Header("Authorization") accessToken: String?
    ): Call<MemeWorldApiResponses>


}