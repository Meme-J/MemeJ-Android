package com.example.memej.body

import androidx.annotation.Keep


@Keep
/**
 * Search body inclusive
 */
data class searchBody(
    val search: String,
    val searchType: String

)