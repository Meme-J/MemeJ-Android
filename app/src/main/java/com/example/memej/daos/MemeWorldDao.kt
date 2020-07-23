package com.example.memej.daos

import androidx.paging.DataSource
import androidx.room.*
import com.example.memej.responses.memeWorldResponses.Meme_World

@Dao
interface MemeWorldDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<Meme_World>)

    @Transaction
    @Query("SELECT * FROM Meme_World ORDER BY lastUpdated DESC")
    fun posts(): DataSource.Factory<Int, Meme_World>

}