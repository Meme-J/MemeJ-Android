package com.example.memej.Utils

import android.preference.PreferenceManager
import com.example.memej.responses.NumLikes
import com.example.memej.responses.ProfileResponse
import com.example.memej.responses.workspaces.UserWorkspaces


/**
 * Preference Util to save the data locally in shared preference
 */
object PreferenceUtil {


    private val pm = PreferenceManager.getDefaultSharedPreferences(ApplicationUtil.instance)

    private const val ID = "_id"
    private const val USERNAME = "username"
    private const val NAME = "name"
    private const val EMAIL = "email"
    private var LIKES = "likes"
    private var CURRENT_SPACE = "current_space"
    private var CURRENT_SPACE_ID = "current_space_id"

    private var HAS_SEEN_WALKTHROUGH = null

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

    var current_space: String?
        get() = pm.getString(CURRENT_SPACE, "Global Space")
        set(value) {
            pm.edit().putString(CURRENT_SPACE, value).apply()
        }

    var current_space_id: String?
        get() = pm.getString(CURRENT_SPACE_ID, "")
        set(value) {
            pm.edit().putString(CURRENT_SPACE_ID, value).apply()
        }


    var likes: Int?
        get() = pm.getInt(LIKES, 0)
        set(value) {
            pm.edit().putInt(LIKES, value!!).apply()
        }


    var hasSeenWalkthrough: Boolean
        get() = pm.getBoolean("HAS_SEEN_WALKTHROUGH", false)
        set(value) {
            pm.edit().putBoolean("HAS_SEEN_WALKTHROUGH", value).apply()
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

    fun setCurrentSpaceFromPreference(userWorkspace: UserWorkspaces.Workspace) {
        val pref = PreferenceUtil
        pref.current_space = userWorkspace.name
        pref.current_space_id = userWorkspace._id

    }

    fun getCurrentSpaceFromPreference(): UserWorkspaces.Workspace? {
        val prefUtil = PreferenceUtil
        return prefUtil.current_space_id?.let {
            prefUtil.current_space?.let { it1 ->
                UserWorkspaces.Workspace(
                    it,
                    it1
                )
            }
        }
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

    fun getWalkthrough(): Boolean {
        val p = PreferenceUtil
        return p.hasSeenWalkthrough
    }

    fun setWalkthroughStatus(hasSeen: Boolean) {
        val pref = PreferenceUtil
        pref.hasSeenWalkthrough = hasSeen
    }

}