package com.example.memej.body


import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class editMemeBody(
    //remeber to give memeId, not template ID
    @field:Json(name = "memeId")
    val memeId: String,                          // 5ebac5fc9434062d5b276ccd
    @field:Json(name = "placeholderText")
    val placeholderText: String,                  // Kavya is my only love
    @field:Json(name = "tags")
    val tags: List<String> = listOf(),      //Can be empty as well                 // Kavya is my only love
    @field:Json(name = "numPlaceholders")
    val numPlaceholders: Int           // Kavya is my only love

)