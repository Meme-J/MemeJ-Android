package com.example.memej.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.memej.MainActivity
import com.example.memej.R
import com.example.memej.Utils.PreferenceUtil
import com.example.memej.Utils.sessionManagers.PreferenceManager


class SplashScreen : AppCompatActivity() {


    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var splash_display_time: Long = 1000
    private val preferenceManager: PreferenceManager = PreferenceManager()
    private val preferencesUtils = PreferenceUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        handler = Handler()

        val preferences =
            getSharedPreferences(getString(R.string.intro_prefs), Context.MODE_PRIVATE)
        val firstRun = preferences.getBoolean(getString(R.string.intro_prefs_first_run), true)

        Log.e("x", "In Splash")
        if (firstRun) {
            startActivity(Intent(this, IntroActivity::class.java))
            finish()


        } else {

            Log.e("x", "In Else")
            val intent = if (preferenceManager.authToken!!.isEmpty()) {
                Log.e("x", "In no access token")
                Intent(this, LoginActivity::class.java)
            } else {
                Log.e("x", "In success atoken")
                Intent(this, MainActivity::class.java)


            }
            handler = Handler()

            runnable = Runnable {
                startActivity(intent)
                finish()
            }

            handler.postDelayed(runnable, splash_display_time)

        }


        handler = Handler()

        runnable = Runnable {
            startActivity(intent)
            finish()
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)

    }
}



