package com.example.memej.ui.workspace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.memej.R
import com.example.memej.databinding.ActivityCreateWorkspaceBinding

class CreateWorkspaceActivity : AppCompatActivity() {

    lateinit var b: ActivityCreateWorkspaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = DataBindingUtil.setContentView(this, R.layout.activity_create_workspace)


    }
}
