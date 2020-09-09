package com.example.memej.body


import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class AcceptRequestsBody(
    @Json(name = "request")
    val request: Request
) {
    @Keep
    data class Request(
        @Json(name = "from")
        val from: String, // kavya
        @Json(name = "_id")
        val id: String, // 5ebc29b313fbe833d78dc971
        @Json(name = "name")
        val name: String // secongworkspace
    )
}