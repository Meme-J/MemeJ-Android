package com.example.memej.entities

import androidx.annotation.Keep

@Keep
data class LoginBody(

    val username: String,
    val password: String
)