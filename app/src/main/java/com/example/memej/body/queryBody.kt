package com.example.memej.body

import androidx.annotation.Keep

@Keep
//Search Query body for the Main Activity
data class queryBody(
    val search: String?
)