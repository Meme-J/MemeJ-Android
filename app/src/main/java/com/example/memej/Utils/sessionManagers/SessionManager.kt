package com.example.memej.Utils.sessionManagers

import android.content.Context
import android.content.SharedPreferences
import com.example.memej.R

class SessionManager(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        //        const val USER_ACCESS_TOKEN = "user_access_token"
//        const val USER_REFRESH_TOKEN = "user_refresh_token"
        val USER_ACCESS_TOKEN = null
        val USER_REFRESH_TOKEN = null
    }

    /**
     * Function to save auth token
     */

    fun saveAuth_access_Token(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_ACCESS_TOKEN, token)
        editor.apply()
    }

    fun saveAuth_refresh_Token(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_REFRESH_TOKEN, token)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */

    fun fetchAcessToken(): String? {
        return prefs.getString(USER_ACCESS_TOKEN, null)
    }

    fun fetchRefreshToken(): String? {
        return prefs.getString(USER_REFRESH_TOKEN, null)
    }

}