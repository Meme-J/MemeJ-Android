package com.example.memej.entities

import androidx.room.Entity
import com.squareup.moshi.Json

@Entity
data class memeTemplate(
    //Class for getting the empty templates of memes of a particular group
    //Will have the original memeId, the image of String
    //This wil have a tag as well associated. (A genre for example stating the tag
    //This will be fetched only when the user crosses the memeGroup Tag, only applicable with internet
//    (This is the response)

    val img_url: String,
    @Json(name = "_id")
    val id: String,                 //Meme Id // 5ebb18b21f22b62ebb8dd2e7
    @Json(name = "lastUpdated")     //TimeStamp
    val lastUpdated: String,
    @Json(name = "numPlaceholders")
    val numPlaceholders: Int, // 3
    @Json(name = "placeholders")        //This is also empty
    val placeholders: List<String>,
    @Json(name = "stage")
    val stage: Int,             //For adding memes, the initial stage will be 0, then, it will be staged to 1
    @Json(name = "tags")
    val tags: List<Any>,
    @Json(name = "templateId")
    val templateId: String,
//    @Json(name = "users")
//    val users: List<ExploreMemes.ExploreMemesItem.User>,
//    @Json(name = "__v")
//    val v: Int, // 0
    val memeGroupId: Int,
    @Json(name = "workspace")
    val workspace: String // Global
)
