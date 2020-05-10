package com.example.memej.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class Users(
    @PrimaryKey
    val id: Int,
    val name: String,
    val userName: String,
    val email: String,
    val password: String
)