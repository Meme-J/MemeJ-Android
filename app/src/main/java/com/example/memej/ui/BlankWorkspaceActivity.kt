package com.example.memej.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.memej.R
import com.example.memej.ui.workspace.CreateWorkspaceActivity

class BlankWorkspaceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blank_workspace)


        //Set default fragmnet

        if (savedInstanceState == null) {
            val frag = CreateWorkspaceActivity()
            supportFragmentManager.beginTransaction().replace(R.id.container_blank_space, frag)
                .commit()
        }

    }
}
