package com.example.memej.responses

import com.example.memej.entities.LoginUser

data class LoginResponse(val error: String, val message: String, val users: LoginUser)