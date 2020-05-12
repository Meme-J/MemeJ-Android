package com.example.memej.daos

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.memej.entities.memeGroup

//Create BoundaryCallbacks
@Dao
interface MemeGroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<memeGroup>)

    @Query("SELECT * FROM memeGroup")
    fun posts(): DataSource.Factory<Int, memeGroup>
}