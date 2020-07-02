package com.example.memej.entities

import androidx.annotation.Keep


@Keep
data class UserBody(
    val name: String,
    val username: String,
    val email: String,
    val password: String
)