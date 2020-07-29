package com.example.memej.ui.drawerItems

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.memej.R
import com.example.memej.databinding.ActivityWorkSpaceBinding

class WorkSpaceActivity : AppCompatActivity() {

    lateinit var binding: ActivityWorkSpaceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_work_space)





    }
}
