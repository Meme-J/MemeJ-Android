package com.example.memej.responses


import android.os.Parcelable
import androidx.room.Entity
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


data class ProfileResponse(
    @Json(name = "profile")
    val profile: Profile
) {
    @Parcelize
    @Entity
    data class Profile(

        @Json(name = "email")
        val email: String, // test3@email.com
        val _id: String, // 5ec63c1cec83c400172ef024
        @Json(name = "name")
        val name: String, // Kavya Goyal
        @Json(name = "username")
        val username: String // test2
    ) : Parcelable
}