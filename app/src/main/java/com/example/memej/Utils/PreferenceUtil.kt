package com.example.memej.Utils

import android.app.Application
import android.preference.PreferenceManager
import com.example.memej.responses.NumLikes
import com.example.memej.responses.ProfileResponse

//Get the context
val ctx = Application().applicationContext


object PreferenceUtil {


    private val pm = PreferenceManager.getDefaultSharedPreferences(ApplicationUtil.instance)

    private const val ID = "_id"
    private const val USERNAME = "username"
    private const val NAME = "name"
    private const val EMAIL = "email"
    private var LIKES = "likes"

    private var hasSeenWalkthrough = false

    var _id: String?
        get() = pm.getString(ID, "")
        set(value) {
            pm.edit().putString(ID, value).apply()
        }

    var username: String?
        get() = pm.getString(USERNAME, "")
        set(value) {
            pm.edit().putString(USERNAME, value).apply()
        }

    var name: String?
        get() = pm.getString(NAME, "")
        set(value) {
            pm.edit().putString(NAME, value).apply()
        }

    var email: String?
        get() = pm.getString(EMAIL, "")
        set(value) {
            pm.edit().putString(EMAIL, value).apply()
        }


    var likes: Int?
        get() = pm.getInt(LIKES, 0)
        set(value) {
            pm.edit().putInt(LIKES, value!!).apply()
        }


    fun setUserFromPreference(user: ProfileResponse.Profile) {
        //Instamce of pref util
        val pref = PreferenceUtil
        pref._id = user._id
        pref.email = user.email
        pref.username = user.username
        pref.name = user.name
    }

    fun getUserFromPrefernece(): ProfileResponse.Profile {
        val prefUtil = PreferenceUtil
        return ProfileResponse.Profile(
            prefUtil.email!!,
            prefUtil._id!!,
            prefUtil.name!!,
            prefUtil.username!!
        )

    }

    fun clearPrefData() {
        pm.edit().clear().apply()
    }

    fun setNumberOfLikesFromPreference(likes: NumLikes) {
        val pref = PreferenceUtil
        pref.likes = likes.likes
    }

    fun getNumberOfLikes(): NumLikes {
        val p = PreferenceUtil
        return NumLikes(p.likes!!)
    }

    fun setStatusOfWalkThroughTrue(): Boolean {
        val p = PreferenceUtil
        return p.hasSeenWalkthrough

    }


}