package com.example.memej.models.responses.auth

import androidx.annotation.Keep


@Keep
data class LoginResponse(val msg: String, val user: LoginUser)