package com.example.memej.responses.workspaces


import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class UserRequestResponse(
    @Json(name = "requests")
    val requests: List<Request>
) {
    @Keep
    data class Request(
        @Json(name = "from")
        val from: String, // KavyaVmatsal
        @Json(name = "_id")
        val id: String, // u
        @Json(name = "name")
        val name: String // secongworkspacee
    )
}