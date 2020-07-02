package com.example.memej.responses

import androidx.annotation.Keep

@Keep
data class LoginUser(
    val username: String,
    val _id: String,
    val accessToken: String,
    val refreshToken: String
)