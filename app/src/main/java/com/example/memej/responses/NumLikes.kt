package com.example.memej.responses


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
data class NumLikes(
    @PrimaryKey(autoGenerate = true)
    @Json(name = "likes")
    val likes: Int // 4
)