package com.example.memej.entities

import androidx.annotation.Keep
import androidx.room.PrimaryKey


@Keep
data class Users(
    @PrimaryKey
    val name: String,
    val userName: String,
    val email: String,
    val password: String
)