package com.example.memej.body


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class SendWorkspaceRequestBody(
    @Json(name = "to")
    val to: List<String>,       //List of Username
    @Json(name = "workspaceId")
    val workspaceId: String,
    @Json(name = "workspaceName")
    val workspaceName: String
) : Parcelable