package com.example.memej.responses.memeWorldResponses

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Coordinate(
    @Json(name = "x")
    val x: Int, // 1
    @Json(name = "y")
    val y: Int // 2
) : Parcelable
