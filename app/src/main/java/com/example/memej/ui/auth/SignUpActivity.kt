package com.example.memej.ui.auth

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.memej.MainActivity
import com.example.memej.R
import com.example.memej.Utils.SaveSharedPreference
import com.example.memej.Utils.SessionManager
import com.example.memej.entities.UserBody
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.SignUpResponse
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    lateinit var etName: TextInputEditText
    lateinit var etUserame: TextInputEditText
    lateinit var etPassword: TextInputEditText
    lateinit var etEmail: TextInputEditText

    lateinit var tName: TextInputLayout
    lateinit var tUname: TextInputLayout
    lateinit var temail: TextInputLayout
    lateinit var tpassword: TextInputLayout

    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        etName = findViewById(R.id.etName)
        etUserame = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        etEmail = findViewById(R.id.etEmail)

        tName = findViewById(R.id.tilName)
        tUname = findViewById(R.id.tilUsername)
        tpassword = findViewById(R.id.tilPassword)
        temail = findViewById(R.id.tilEmail)


        sessionManager = SessionManager(this)
        val b: MaterialButton = findViewById(R.id.signUp_btn)
        b.setOnClickListener {
            //Valiate the details
            // if (validateDetails()) {
            //Check internet connection
            if (isNetworkConnected()) {
                postSignUp()
            } else {
                AlertDialog.Builder(this).setTitle("No Internet Connection")
                    .setMessage("Please check your internet connection and try again")
                    .setPositiveButton(android.R.string.ok) { _, _ -> }
                    .setIcon(android.R.drawable.ic_dialog_alert).show()

            }


            //}

        }

        val tl = findViewById<MaterialTextView>(R.id.tv_loginInstead)
        tl.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }


    }

    private fun postSignUp() {
        //Create instances
        val service = RetrofitClient.getAuthInstance()
        val regInfo = UserBody(
            etName.text.toString(),
            etUserame.text.toString(),
            etEmail.text.toString(),
            etPassword.text.toString()
        )
        service.createUser(regInfo).enqueue(object : retrofit2.Callback<SignUpResponse> {
            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Toast.makeText(this@SignUpActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<SignUpResponse>,
                response: Response<SignUpResponse>
            ) {
                if (response.body()?.msg == "Registeration successful") {
                    //Code for successful creation of user
                    Toast.makeText(
                        this@SignUpActivity,
                        "Registration Successful",
                        Toast.LENGTH_SHORT
                    ).show()
                    //GEt the access token and the id
                    //Save the acess tokens
//                    sessionManager.saveAuth_access_Token(
//                        SignUpResponse(
//                            response.body()!!.msg,
//                            response.body()!!.user
//                        ).user.accessToken
//                    )
//                    sessionManager.saveAuth_refresh_Token(
//                        (LoginResponse(
//                            response.body()!!.msg,
//                            response.body()!!.user
//                        )).user.refreshToken
//                    )

                    goToMainActivity()
                } else {

                    Toast.makeText(this@SignUpActivity, response.body()?.msg, Toast.LENGTH_SHORT)
                        .show()

                }

            }
        })


    }

    private fun goToMainActivity() {
        SaveSharedPreference().setLoggedIn(applicationContext, true)
        val i = Intent(this, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(i)
    }

    private fun validateDetails(): Boolean {
        var isValid = true


        val nameTemp = etName.toString().replace("\\s".toRegex(), "")
        if (etName.length() == 0) {
            tName.error = getString(R.string.name_empty)
            isValid = false
        } else if (nameTemp.length < 2 || nameTemp.length > 31) {
            tName.error = getString(R.string.name_length)
            isValid = false
        } else if (!validateName(etName.toString())) {
            tName.error = getString(R.string.name_invalid)
            isValid = false
        }

        if (etUsername.length() == 0) {
            tUname.error = getString(R.string.username_empty)
            isValid = false
        } else if (!validateUsername(etUsername.toString())) {
            tUname.error = getString(R.string.username_invalid)
            isValid = false
        }


        if (etEmail.length() == 0) {
            temail.error = getString(R.string.email_empty)
            isValid = false
        } else if (!validateEmail(etEmail.toString())) {
            temail.error = getString(R.string.email_invalid)
            isValid = false
        }


        if (etPassword.length() == 0) {
            tpassword.error = getString(R.string.pwd_empty)
            isValid = false
        } else if (etPassword.length() < 6) {
            tpassword.error = getString(R.string.pwd_length_min)
            isValid = false
        } else if (etPassword.length() > 16) {
            tpassword.error = getString(R.string.pwd_length_max)
            isValid = false
        }



        return isValid
    }

    private fun validateName(string: String): Boolean {
        //Check if the name has only alphabets and not special characters or numbers
        for (c in string) {
            if (c !in 'A'..'Z' && c !in 'a'..'z' && c != ' ') {
                return false
            }
        }
        return true
    }

    //
    private fun validateUsername(string: String): Boolean {
        //Check the validity of the username
        for (c in string) {
            if (c !in 'A'..'Z' && c !in 'a'..'z' && c !in '0'..'9') {
                return false
            }
        }
        return true
    }

    private fun validateEmail(email: String): Boolean {
        val EMAIL_REGEX = "^[A-Za-z](.*)([@])(.{1,})(\\.)(.{1,})"
        return EMAIL_REGEX.toRegex().matches(email)
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




