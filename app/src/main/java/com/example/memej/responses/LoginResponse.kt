package com.example.memej.responses

import androidx.annotation.Keep


@Keep
data class LoginResponse(val msg: String, val user: LoginUser)