package com.example.memej.responses

import android.accessibilityservice.GestureDescription

data class LoginUser(
    val username: String,
    val _id: String,
    val accessToken: String,
    val refreshToken: GestureDescription.StrokeDescription
)