package com.example.memej.ui.auth

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.example.memej.MainActivity
import com.example.memej.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.SignInButton
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {


    lateinit var etUsername: TextInputEditText
    lateinit var etPassword: TextInputEditText
    lateinit var t1: TextInputLayout
    lateinit var t2: TextInputLayout

    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Login Activity
        //Get Ids
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        t1 = findViewById(R.id.tilUsername)
        t2 = findViewById(R.id.tilPassword)

        val pb_login = findViewById<ProgressBar>(R.id.pb_login)

        //Functions for Google SignIn
        val googleSignInOptions: GoogleSignInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val mGoogleApiClient = GoogleSignIn.getClient(this, googleSignInOptions)


//        val googleApiClient = GoogleApi
//            .Builder(this)
//            .enableAutoManage(this,GoogleApiClient.OnConnectionFailedListener())
//            .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
//            .build()


        //OnClick on the Login button
        val b: MaterialButton = findViewById(R.id.login_btn)
        b.setOnClickListener {
            //Validate weather the fields are full or not
            if (validateDetail()) {
                //Check for internet connection now
                if (isNetworkConnected()) {
                    //Validate the network call
                    pb_login.visibility = View.VISIBLE
                    //postLogin()
                }
                //In case their is no internet connection
                else {
                    AlertDialog.Builder(this).setTitle("No Internet Connection")
                        .setMessage("Please check your internet connection and try again")
                        .setPositiveButton(android.R.string.ok) { _, _ -> }
                        .setIcon(android.R.drawable.ic_dialog_alert).show()
                }
            }

        }


        val b2: SignInButton = findViewById(R.id.Gsign_in_button)
        b2.setOnClickListener {
            //Use Google Sign In
//            val intent: Intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
//            startActivityForResult(intent, RC_SIGN_IN)

        }


    }


//     override fun onConnectionFailed(p0: ConnectionResult) {
//      Log.d("ConnectionResult", connectionResult.toString())
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == RC_SIGN_IN) {
//            val result: GoogleSignInResult? = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
//            handleSignInResult(result!!)
//        }
//    }

    private fun handleSignInResult(signInResult: GoogleSignInResult) {
        if (signInResult.isSuccess) {
            // Authenticated
            val account: GoogleSignInAccount? = signInResult.signInAccount
            //Get Intents
            if (account != null) {
                //Create bundle to pass information
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                val bundleLogin = bundleOf(
                    "name" to account.displayName,
                    "email" to account.email,
                    "url" to account.photoUrl.toString()
                )

                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()

            }
        } else {
            //When not able to get the account
        }
    }


    private fun validateDetail(): Boolean {
        var isValid = true
        if (etUsername.length() == 0) {
            t1.error = getString(R.string.username_empty)
            isValid = false
        }
        if (etPassword.length() == 0) {
            t2.error = getString(R.string.pwd_empty)
            isValid = false
        }

        return isValid
    }

    private fun isNetworkConnected(): Boolean {

        val connectivityManager =
            this?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }


}


