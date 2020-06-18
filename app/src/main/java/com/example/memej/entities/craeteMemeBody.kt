package com.example.memej.entities

import com.squareup.moshi.Json

data class craeteMemeBody(
    @Json(name = "memeId")
    val templateId: String,                          // 5ebac5fc9434062d5b276ccd
    @Json(name = "placeholderText")
    val placeholderText: String,                  // Kavya is my only love
    @Json(name = "tags")
    val tags: List<String> = listOf(),      //Can be empty as well                 // Kavya is my only love
    @Json(name = "numPlaceholders")
    val numPlaceholders: Int           // Kavya is my only love

)