package com.example.memej.body

import androidx.annotation.Keep


@Keep
data class UserBody(
    val name: String,
    val username: String,
    val email: String,
    val password: String
)