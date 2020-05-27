package com.example.memej.responses

data class LoginUser(
    val username: String,
    val _id: String,
    val accessToken: String,
    val refreshToken: String
)