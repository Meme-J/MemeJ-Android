package com.example.memej.entities


import com.squareup.moshi.Json

data class editMemeBody(
    //remeber to give memeId, not template ID
    @Json(name = "memeId")
    val memeId: String,                          // 5ebac5fc9434062d5b276ccd
    @Json(name = "placeholderText")
    val placeholderText: String,                  // Kavya is my only love
    @Json(name = "tags")
    val tags: List<String>                  // Kavya is my only love

)