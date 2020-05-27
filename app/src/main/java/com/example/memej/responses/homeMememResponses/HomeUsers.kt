package com.example.memej.responses.homeMememResponses


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HomeUsers(
    @Json(name = "_id")
    val id: String, // 5ebb18141f22b62ebb8dd2e4
    @Json(name = "username")
    val username: String // KavyaVmatsal
) : Parcelable
