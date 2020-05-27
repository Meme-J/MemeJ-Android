package com.example.memej.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.memej.MainActivity
import com.example.memej.R
import com.example.memej.Utils.SaveSharedPreference


class SplashScreen : AppCompatActivity() {


    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        handler = Handler()
        handler.postDelayed({

            //We ll write some lines of code in this to see weather the user is logged in or not
            //To redirect to signUp/Login Page or go to the home page fragment

            //Refresh tokens is stored in database


            // Test for going to the login activity

            if (SaveSharedPreference().getLoggedStatus(applicationContext)) {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            } else {
                val i = Intent(applicationContext, LoginActivity::class.java)
                startActivity(i)
            }

            finish()





        }, 1000)     //1 seconds delay


    }
}



