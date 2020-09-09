package com.example.memej.body


import androidx.annotation.Keep
import com.squareup.moshi.Json


@Keep
data class createMemeBody(

    @field:Json(name = "numPlaceholders")
    val numPlaceholders: Int, // 4
    @field:Json(name = "placeholderText")
    val placeholderText: String, // Kavya is the reigning queen
    @field:Json(name = "tags")
    val tags: List<String>,
    @field:Json(name = "templateId")
    val templateId: String // exampletemplateid
)