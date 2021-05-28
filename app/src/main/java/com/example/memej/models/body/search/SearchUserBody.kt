package com.example.memej.models.body.search


import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class SearchUserBody(
    @Json(name = "search")
    val search: String // kavya
)