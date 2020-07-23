package com.example.memej.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.memej.Utils.RoomConvertor
import com.example.memej.daos.MyMemesDao
import com.example.memej.responses.memeWorldResponses.Meme_World


@Database(entities = [Meme_World::class], version = 1, exportSchema = false)
@TypeConverters(RoomConvertor::class)
abstract class MyMemesDatabase : RoomDatabase() {

    abstract fun myMemesDao(): MyMemesDao

    companion object {
        @Volatile
        private var instance: MyMemesDatabase? = null

        fun getDatabase(context: Context): MyMemesDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, MyMemesDatabase::class.java, "MY_MEMES_DATABASE")
                .fallbackToDestructiveMigration()
                .build()
    }

}