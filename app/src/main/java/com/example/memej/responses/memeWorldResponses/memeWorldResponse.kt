package com.example.memej.responses.memeWorldResponses


import com.squareup.moshi.Json

//Parameters to use are meme, lasMemeId in the user body class
data class memeWorldResponse(
    @Json(name = "lastMemeId")
    val lastMemeId: String, // 5ebb31e70afd112f7667d7c4
    @Json(name = "memes")
    val memes: List<Meme>
)


