package com.example.memej.responses


import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Keep
data class ProfileResponse(
    @field:Json(name = "profile")
    val profile: Profile
) {
    @Parcelize
    @Entity
    data class Profile(

        @field:Json(name = "email")
        val email: String, // test3@email.com
        val _id: String, // 5ec63c1cec83c400172ef024
        @field:Json(name = "name")
        val name: String, // Kavya Goyal
        @field:Json(name = "username")
        val username: String // test2
    ) : Parcelable
}