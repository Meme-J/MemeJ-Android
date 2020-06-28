package com.example.memej.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.memej.MainActivity
import com.example.memej.R
import com.example.memej.Utils.sessionManagers.PreferenceManager


class SplashScreen : AppCompatActivity() {


    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var splash_display_time: Long = 1000
    private val preferenceManager: PreferenceManager = PreferenceManager()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val intent = if (preferenceManager.authToken!!.isEmpty()) {
            Intent(this, LoginActivity::class.java)
        } else {
            Intent(this, MainActivity::class.java)
        }

        //Refactor to using access token
        runnable = Runnable {
            startActivity(intent)
            finish()
        }
        handler = Handler()
        handler.postDelayed(runnable, splash_display_time)


    }


    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}



