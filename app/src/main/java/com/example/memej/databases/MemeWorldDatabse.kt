package com.example.memej.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.memej.Utils.RoomConvertor
import com.example.memej.daos.MemeWorldDao
import com.example.memej.responses.memeWorldResponses.Meme_World


@Database(entities = [Meme_World::class], version = 1, exportSchema = false)
@TypeConverters(RoomConvertor::class)

abstract class MemeWorldDatabse : RoomDatabase() {

    abstract fun memeWorldDao(): MemeWorldDao

    companion object {
        @Volatile
        private var instance: MemeWorldDatabse? = null

        fun getDatabase(context: Context): MemeWorldDatabse =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, MemeWorldDatabse::class.java, "MEME_WORLD_DATABASE")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
    }

}