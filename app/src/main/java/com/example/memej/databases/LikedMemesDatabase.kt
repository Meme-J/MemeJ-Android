package com.example.memej.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.memej.Utils.RoomConvertor
import com.example.memej.daos.LikedMemesDao
import com.example.memej.responses.memeWorldResponses.Meme_World

@Database(entities = [Meme_World::class], version = 1, exportSchema = false)
@TypeConverters(RoomConvertor::class)
abstract class LikedMemesDatabase : RoomDatabase() {

    abstract fun likedMemesDao(): LikedMemesDao

    companion object {
        @Volatile
        private var instance: LikedMemesDatabase? = null

        fun getDatabase(context: Context): LikedMemesDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, LikedMemesDatabase::class.java, "LIKED_MEMES_DATABASE")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
    }

}