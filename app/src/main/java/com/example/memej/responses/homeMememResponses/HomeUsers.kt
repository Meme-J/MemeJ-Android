package com.example.memej.responses.homeMememResponses


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HomeUsers(

    val _id: String, // 5ebb18141f22b62ebb8dd2e4
    @field:Json(name = "username")
    val username: String // KavyaVmatsal
) : Parcelable
