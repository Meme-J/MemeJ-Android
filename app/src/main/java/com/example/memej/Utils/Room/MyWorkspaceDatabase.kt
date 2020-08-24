package com.example.memej.Utils.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.memej.responses.workspaces.UserWorkspaces

@Database(entities = [UserWorkspaces.Workspace::class], version = 1, exportSchema = false)
abstract class MyWorkspaceDatabase : RoomDatabase() {

    abstract fun myWorkspacesDao(): MyWorspaceDao

    companion object {
        @Volatile
        private var instance: MyWorkspaceDatabase? = null

        fun getDatabase(context: Context): MyWorkspaceDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(
                appContext,
                MyWorkspaceDatabase::class.java,
                "MY_WORKSPACES_DATABASE"
            )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
    }


}