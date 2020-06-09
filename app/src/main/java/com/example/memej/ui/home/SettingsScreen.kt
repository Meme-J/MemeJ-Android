package com.example.memej.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.memej.R
import com.example.memej.Utils.SaveSharedPreference
import com.example.memej.databinding.ActivitySettingsScreenBinding
import com.example.memej.ui.auth.LoginActivity

class SettingsScreen : AppCompatActivity() {


    lateinit var binding: ActivitySettingsScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings_screen)

        //Implement add avatar

        binding.settingsLogout.setOnClickListener {
            //Set logged in status as false
            SaveSharedPreference().setLoggedIn(applicationContext, false)
            val i = Intent(this@SettingsScreen, LoginActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            finishAffinity()
            startActivity(i)

//            logout()
        }


    }

//    private fun logout() {
//        val service = RetrofitClient.getAuthInstance()
//        val logout: Call<Void?>? = service.logout()
//        logout?.enqueue(object : Callback<Void?> {
//
//            override fun onFailure(call: Call<Void?>, t: Throwable) {
//                Log.e("Logout", "Failed {$t")
//            }
//
//            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
//
//                //Apply required if statemnet
//                //Use response body message
//                SaveSharedPreference().setLoggedIn(applicationContext, false)
//                //Invalidate access tokens
//
//                val i = Intent(this@SettingsScreen, LoginActivity::class.java)
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(i)
//
//            }
//        })
//    }
}
