package com.example.memej.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.memej.R

class BaseActivityHost : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_host)

    }
}
