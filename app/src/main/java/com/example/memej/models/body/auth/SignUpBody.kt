package com.example.memej.models.body.auth

import androidx.annotation.Keep


@Keep
data class SignUpBody(
    val name: String,
    val username: String,
    val email: String,
    val password: String
)