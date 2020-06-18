package com.example.memej.ui.auth

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.memej.MainActivity
import com.example.memej.R
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SaveSharedPreference
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.entities.LoginBody
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.LoginResponse
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Response


class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    //Instanec of Shared Preferences


    lateinit var etUsername: TextInputEditText
    lateinit var etPassword: TextInputEditText
    lateinit var t1: TextInputLayout
    lateinit var t2: TextInputLayout
    lateinit var pb: ProgressBar
    private lateinit var sessionManager: SessionManager


    //parameters for google signIn
    lateinit var signInBtn: SignInButton
    lateinit var googleApiClient: GoogleApiClient
    private val RC_SIGNIN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Login Activity
        //Get Ids
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        t1 = findViewById(R.id.tilUsername)
        t2 = findViewById(R.id.tilPassword)

        pb = findViewById(R.id.pb_login)
        sessionManager =
            SessionManager(this)

        //Functions for Google SignIn

        val googleSignInOptions: GoogleSignInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        ///Create the api for login
        val service = RetrofitClient.getAuthInstance()
        googleApiClient = GoogleApiClient.Builder(this).enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build()

//        signInBtn = findViewById(R.id.google_login)
//        signInBtn.setOnClickListener{
//            val i = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
//            startActivityForResult(i, RC_SIGNIN)
//
//            //Overridden method on activty result
//        }


        //OnClick on the Login button
        val b: MaterialButton = findViewById(R.id.login_btn)
        b.setOnClickListener {
            //Validate weather the fields are full or not
            if (validateDetail()) {
                //Check for internet connection now
                if (isNetworkConnected()) {
                    //Validate the network call
                    Log.e("K", "In Okay state")
                    pb.visibility = View.VISIBLE
                    postLogin()

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

        val ts: MaterialTextView = findViewById(R.id.tv_signUpInstead)
        ts.setOnClickListener {
            val i = Intent(this, SignUpActivity::class.java)
            startActivity(i)
        }
//        val b2: SignInButton = findViewById(R.id.Gsign_in_button)
//        b2.setOnClickListener {
        //Use Google Sign In
//            val intent: Intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
//            startActivityForResult(intent, RC_SIGN_IN)

    }

    private fun postLogin() {
        //Account id = sth
        val service = RetrofitClient.getAuthInstance()
        val inf = LoginBody(
            etUsername.text.toString(),
            etPassword.text.toString()
        )
        Log.e("login", "info" + inf.toString())

        service.loginUser(inf).enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {


                val message = ErrorStatesResponse.returnStateMessageForThrowable(t)
                val snack = Snackbar.make(container_signUp, message, Snackbar.LENGTH_SHORT)
                snack.show()
                pb.visibility = View.GONE
                pb.visibility = View.GONE


            }


            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                if (response.isSuccessful) {
                    if (response.body()?.msg == "Login successful.") {
                        Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT)
                            .show()
                        Log.e("Login", response.message().toString())

                        //Set looged in status true
                        //Store the access token on every login
                        //Get the access token on login
                        sessionManager.saveAuth_access_Token(
                            LoginResponse(
                                response.body()!!.msg,
                                response.body()!!.user
                            ).user.accessToken
                        )
                        sessionManager.saveAuth_refresh_Token(
                            (LoginResponse(
                                response.body()!!.msg,
                                response.body()!!.user
                            )).user.refreshToken
                        )

                        goToMainActivity()
                    }
                    //Incorrect cred format
                    else {
                        val snack = Snackbar.make(
                            signinView,
                            response.body()?.msg.toString(),
                            Snackbar.LENGTH_SHORT
                        )
                        snack.show()
                        pb.visibility = View.GONE
                    }

                } else {
                    //Stop the spinner
                    pb_login.visibility = View.GONE
                    Toast.makeText(
                        this@LoginActivity,
                        response.errorBody().toString(),
                        Toast.LENGTH_SHORT
                    )
                        .show()

                }
            }
        })
    }

    private fun goToMainActivity() {
        //Turn of the progress bar

        pb.visibility = View.GONE
        SaveSharedPreference()
            .setLoggedIn(applicationContext, true)
        val i = Intent(this, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(i)

    }


//     override fun onConnectionFailed(p0: ConnectionResult) {
//      Log.d("ConnectionResult", connectionResult.toString())
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGNIN) {
            val result: GoogleSignInResult? = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result != null) {
                if (result.isSuccess) {
                    //Intent to the main activity with all the requirements

                    goToMainActivity()
                }

            } else {
                Toast.makeText(this, "Unable to login", Toast.LENGTH_SHORT).show()
            }
        }
    }
//
//    private fun handleSignInResult(signInResult: GoogleSignInResult) {
//        if (signInResult.isSuccess) {
//            // Authenticated
//            val account: GoogleSignInAccount? = signInResult.signInAccount
//            //Get Intents
//            if (account != null) {
//                //Create bundle to pass information
//                val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                val bundleLogin = bundleOf(
//                    "name" to account.displayName,
//                    "email" to account.email,
//                    "url" to account.photoUrl.toString()
//                )
//
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                startActivity(intent)
//                finish()
//
//            }
//        } else {
//            //When not able to get the account
//        }
//    }


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
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }


}


