package com.example.memej.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class homeMeme(
    @PrimaryKey
    val img_url: String,
    val memeGroupId: Int,
    val memeId: Int,
    val tag: String,               //For the tag, appended string.
    //val textboxes : TextBox,        //Field of type TextBox
    val memeCheckCount: Int,           //A paramter to check on what state of text box the meme is on
    val num_tb: Int,
    //Order of the coordinates if [num_tb][4] where xTL, yTL, xBR, yBR
    //For text, num of TB = 2;
    //Store them in a better data structure
    val x1: Float,
    val y1: Float,
    val x2: Float,
    val y2: Float,
    val c1: String,    //Color of TB1
    val c2: String,
    val timestamp: String //Time of launch of the meme
    //A username list of all the people acting on it, (Array of usernames and image profile associated with it)

)

//val timezones : ArrayList<String>,
//c
//val translations: Translations,
//@SerializedName("languages")
//val language: List<Language>
//) : Serializable