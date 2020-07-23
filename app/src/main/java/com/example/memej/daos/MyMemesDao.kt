package com.example.memej.daos

import androidx.paging.DataSource
import androidx.room.*
import com.example.memej.responses.memeWorldResponses.Meme_World

@Dao
interface MyMemesDao {

//    //Local
//    @Query("SELECT * FROM Meme_World")
//    fun getAll(): LiveData<List<Meme_World>>
//
//    //Remote
//    //Local with paging, takes the int key with scrolling
//    @Query("SELECT * FROM Meme_World")
//    fun getAllPaged(): DataSource.Factory<Int, Meme_World>
//
//    @Insert
//    fun insertAll(persons: List<Meme_World>)
//
//    @Delete
//    fun delete(person: Meme_World)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<Meme_World>)

    @Transaction
    @Query("SELECT * FROM Meme_World")
    fun posts(): DataSource.Factory<Int, Meme_World>

}