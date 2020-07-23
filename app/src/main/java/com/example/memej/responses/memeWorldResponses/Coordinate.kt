package com.example.memej.responses.memeWorldResponses

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
@JsonClass(generateAdapter = true)
data class Coordinate(


    @field:Json(name = "x")
    var x: Int = 0, // 1
    @field:Json(name = "y")
    var y: Int = 0 // 2


) : Parcelable {
    constructor() : this(0, 0)
}
