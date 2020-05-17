package com.example.memej.ui.profile

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.adapters.ChooseAvatarAdapter
import com.example.memej.adapters.OnItemClickListenerAvatar
import com.example.memej.entities.avatars
import com.example.memej.interfaces.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException


//Create tha adapter
class ChooseAvatar : AppCompatActivity(), OnItemClickListenerAvatar {

    lateinit var rv: RecyclerView
    lateinit var a: ChooseAvatarAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_avatar)

        rv = findViewById(R.id.rv_avatars)
        a = ChooseAvatarAdapter(this)
        rv.layoutManager = GridLayoutManager(this, 5)
        rv.adapter = a
        //Load the images
        if (isNetworkConnected()) {
            val service = RetrofitClient.profileCalls()
            CoroutineScope(Dispatchers.IO).launch {
                val response = service.getAvatars()
                withContext(Dispatchers.Main) {
                    try {
                        if (response.isSuccessful) {
                            Toast.makeText(this@ChooseAvatar, "Loading...", Toast.LENGTH_SHORT)
                                .show()
                            a.setItems(response.body()!!)


                        } else {

                            Toast.makeText(
                                this@ChooseAvatar,
                                "Error: ${response.code()}",
                                Toast.LENGTH_SHORT
                            )
                                .show()

                        }

                    } catch (e: HttpException) {
                        Toast.makeText(
                            this@ChooseAvatar,
                            "Exception ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Throwable) {
                        Toast.makeText(
                            this@ChooseAvatar,
                            "Something went wrong",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }
            }


        } else {
            AlertDialog.Builder(this).setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again")
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .setIcon(android.R.drawable.ic_dialog_alert).show()

        }


    }

    override fun onItemClicked(_avatars: avatars) {


        //Each avatar is an object
        //Take the avatar to the profile fragment
        //Navigate to the fragment
//        val i = Intent(this, MainActivity::class.java)
//        startActivity(i)
//        val frag = ProfileFragment()
//        supportFragmentManager.beginTransaction().replace(R.id.container, frag ).commit()
    }


    private fun isNetworkConnected(): Boolean {

        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

}



