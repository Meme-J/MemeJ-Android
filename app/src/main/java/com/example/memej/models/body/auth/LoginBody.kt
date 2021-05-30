package com.example.memej.models.body.auth

import androidx.annotation.Keep

@Keep
data class LoginBody(

    val username: String,
    val password: String
)