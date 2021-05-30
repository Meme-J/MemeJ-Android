package com.example.memej.models.body.search

import androidx.annotation.Keep


@Keep
/**
 * Search body inclusive
 */
data class SearchBody(
    val search: String,
    val searchType: String

)