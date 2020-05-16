package com.example.memej.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class memeGroup(
    @PrimaryKey
    //The tag will be sent for the next activity and loaded as well
    //This will pe paginated as well
    val img_url: String,            //Image
    val tag: String,                //Tag [ Array] of anything
    val memeGroupId: Int            //equivealent to templateId
)