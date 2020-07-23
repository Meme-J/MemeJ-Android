package com.example.memej.responses.memeWorldResponses

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
@JsonClass(generateAdapter = true)
data class User(

    var _id: String = " ", // 5ebb18141f22b62ebb8dd2e4

    @field:Json(name = "username")
    var username: String = " " // KavyaVmatsal
) : Parcelable {
    constructor() : this("", "")
}


