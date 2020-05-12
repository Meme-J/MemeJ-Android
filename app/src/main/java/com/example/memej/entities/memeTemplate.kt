package com.example.memej.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class memeTemplate(
    //Class for getting the empty templates of memes of a particular group
    //Will have the original memeId, the image of String
    //This wil have a tag as well associated. (A genre for example stating the tag
    //This will be fetched only when the user crosses the memeGroup Tag, only applicable with internet
    @PrimaryKey
    val memeId: Int,
    val img_url: String,
    val memeTagString: String
)
