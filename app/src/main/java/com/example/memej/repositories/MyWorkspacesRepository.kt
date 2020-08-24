package com.example.memej.repositories

import android.content.Context
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.interfaces.workspaces

class MyWorkspacesRepository constructor(
    private val service: workspaces,
    private val ctx: Context
) {

    private val sessionManager = SessionManager(ctx)


    //This is the overview

}