package com.example.memej.Utils.Room

import androidx.room.*
import com.example.memej.responses.workspaces.UserWorkspaces


@Dao
interface MyWorspaceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertObject(space: UserWorkspaces.Workspace)

    @Delete
    fun deleteObject(space: UserWorkspaces.Workspace)

    @Update
    fun updateObject(space: UserWorkspaces.Workspace)

}