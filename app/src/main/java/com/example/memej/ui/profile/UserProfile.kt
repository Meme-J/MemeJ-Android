package com.example.memej.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.memej.R

class UserProfile : AppCompatActivity() {

    lateinit var arg: Bundle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        //This activity will receive the profile
        arg = savedInstanceState!!
        //Adapt the profile accordingly


    }
}
