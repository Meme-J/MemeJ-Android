package com.example.memej.body

import androidx.annotation.Keep

@Keep
data class LoginBody(

    val username: String,
    val password: String
)