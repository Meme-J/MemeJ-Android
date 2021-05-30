package com.example.memej.models.body.search

import androidx.annotation.Keep

@Keep
//Search Query body for the Main Activity
data class QueryBody(
    val search: String?
)