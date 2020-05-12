package com.example.memej.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.memej.daos.MemeGroupDao
import com.example.memej.entities.memeGroup

@Database(
    entities = [memeGroup::class],
    version = 1,
    exportSchema = false
)
abstract class MemeGroupDatabase : RoomDatabase() {

    companion object {
        fun create(context: Context): MemeGroupDatabase {
            val databaseBuilder =
                Room.databaseBuilder(context, MemeGroupDatabase::class.java, "Meme Group Database")
            return databaseBuilder.build()
        }
    }

    abstract fun postDao(): MemeGroupDao
}
