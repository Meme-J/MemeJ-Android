package com.example.memej.Utils.sessionManagers

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

//import androidx.preference.PreferenceManager

const val LOGGED_IN_PREF = "logged_in_status"

//This is not useful when cache is cleared
class SaveSharedPreference {

    fun getPreferences(context: Context?): SharedPreferences? {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun setLoggedIn(context: Context?, loggedIn: Boolean) {
        val editor = getPreferences(context)!!.edit()
        editor.putBoolean(LOGGED_IN_PREF, loggedIn)
        editor.apply()
    }

    fun getLoggedStatus(context: Context?): Boolean {
        return getPreferences(context)!!.getBoolean(LOGGED_IN_PREF, false)
    }


}