package com.example.memej.models.responses.home


import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class HomeUsers(

    val _id: String, // 5ebb18141f22b62ebb8dd2e4
    @field:Json(name = "username")
    val username: String // KavyaVmatsal
) : Parcelable