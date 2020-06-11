package com.example.memej.entities


import com.squareup.moshi.Json

data class createMemeBody(
    @Json(name = "numPlaceholders")
    val numPlaceholders: Int, // 4
    @Json(name = "placeholderText")
    val placeholderText: String, // Kavya is the reigning queen
    @Json(name = "tags")
    val tags: List<String>,
    @Json(name = "templateId")
    val templateId: String // exampletemplateid
)