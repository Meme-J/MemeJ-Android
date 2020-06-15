package com.example.memej.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.memej.MainActivity
import com.example.memej.R
import com.example.memej.Utils.PreferenceUtil
import com.example.memej.Utils.sessionManagers.SaveSharedPreference
import com.example.memej.databinding.ActivitySettingsScreenBinding
import com.example.memej.ui.auth.LoginActivity
import com.shreyaspatil.MaterialDialog.MaterialDialog


class SettingsScreen : AppCompatActivity() {


    lateinit var binding: ActivitySettingsScreenBinding
    lateinit var toolbar: androidx.appcompat.widget.Toolbar

    private val preferenceUtils = PreferenceUtil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings_screen)
        toolbar = binding.tbSettings
        //Implement add avatar

        toolbar.setNavigationOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)

        }

        binding.settingsLogout.setOnClickListener {
            //Set logged in status as false

            //Ask the user
            val mDialog = MaterialDialog.Builder(this)
                .setTitle("Logout?")
                .setMessage("Are you sure want to logout?")
                .setCancelable(false)
                .setPositiveButton(
                    "Yes",
                    R.drawable.ic_exit_to_app_black_24dp
                ) { dialogInterface, which ->
                    logout()
                }
                .setNegativeButton(
                    "No",
                    R.drawable.ic_close
                ) { dialogInterface, which -> dialogInterface.dismiss() }
                .build()
            mDialog.show()

        }


    }

    private fun logout() {
        SaveSharedPreference()
            .setLoggedIn(applicationContext, false)

        //Remove the saved profile
        preferenceUtils.clearPrefData()
        val i = Intent(this@SettingsScreen, LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        finishAffinity()
        startActivity(i)

    }


}
