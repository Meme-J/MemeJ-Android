package com.example.memej.entities

import androidx.annotation.Keep


@Keep
data class searchBody(
    val search: String,
    val searchType: String

)