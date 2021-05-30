package com.example.memej.models.responses.home

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
data class Coordinates(
    @field:Json(name = "x")
    val x: Int, // 1
    @field:Json(name = "y")
    val y: Int // 2
) : Parcelable
