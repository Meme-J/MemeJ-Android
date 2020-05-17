package com.example.memej.interfaces

import com.example.memej.entities.avatars
import retrofit2.Response
import retrofit2.http.GET

interface Profile {

    //GEt the avatars. Test URl
    @GET("5ec12768a47fdd6af164aad0")
    suspend fun getAvatars(): Response<List<avatars>>


}