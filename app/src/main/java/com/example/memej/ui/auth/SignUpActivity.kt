package com.example.memej.ui.auth

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.memej.R
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SaveSharedPreference
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.entities.UserBody
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.SignUpResponse
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
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
    lateinit var etCnfPwd: TextInputEditText

    lateinit var tName: TextInputLayout
    lateinit var tUname: TextInputLayout
    lateinit var temail: TextInputLayout
    lateinit var tpassword: TextInputLayout
    lateinit var tConf: TextInputLayout

    lateinit var pb: ProgressBar
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        etName = findViewById(R.id.etName)
        etUserame = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        etEmail = findViewById(R.id.etEmail)
        etCnfPwd = findViewById(R.id.etConfPassword)

        tName = findViewById(R.id.tilName)
        tUname = findViewById(R.id.tilUsername)
        tpassword = findViewById(R.id.tilPassword)
        temail = findViewById(R.id.tilEmail)
        tConf = findViewById(R.id.tilConfPassword)

        pb = findViewById(R.id.pb_signUp)
        sessionManager =
            SessionManager(this)
        val b: MaterialButton = findViewById(R.id.signUp_btn)
        b.setOnClickListener {
            //Valiate the details
            if (validateDetails()) {
                //Check internet connection
                if (isNetworkConnected()) {
                    pb.visibility = View.VISIBLE
                    postSignUp()
                } else {
                    AlertDialog.Builder(this).setTitle("No Internet Connection")
                        .setMessage("Please check your internet connection and try again")
                        .setPositiveButton(android.R.string.ok) { _, _ -> }
                        .setIcon(android.R.drawable.ic_dialog_alert).show()

                }


            }

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
        Log.e("SignUp inf ", regInfo.toString())

        service.createUser(regInfo).enqueue(object : retrofit2.Callback<SignUpResponse> {
            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {

                val message = ErrorStatesResponse.returnStateMessageForThrowable(t)
                val snack = Snackbar.make(container_signUp, message, Snackbar.LENGTH_SHORT)
                snack.show()
                pb.visibility = View.GONE

                pb.visibility = View.GONE
            }

            override fun onResponse(
                call: Call<SignUpResponse>,
                response: Response<SignUpResponse>
            ) {

                if (response.isSuccessful) {

                    if (response.body()?.msg == "Registeration successful") {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Registration Successful",
                            Toast.LENGTH_SHORT
                        ).show()

                        goToLoginActivity()

                    } else {

                        val snack = Snackbar.make(
                            container_signUp,
                            response.body()?.msg.toString(),
                            Snackbar.LENGTH_SHORT
                        )
                        snack.show()
                        pb.visibility = View.GONE
                    }

                }
                //Failing/ Error
                else {
                    Log.e("SignIUp", response.errorBody()!!.toString())
                    pb.visibility = View.GONE


                    Toast.makeText(
                        this@SignUpActivity,
                        getString(R.string.signin_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
        )


    }

    private fun goToLoginActivity() {
        SaveSharedPreference()
            .setLoggedIn(applicationContext, true)
        val i = Intent(this, LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(i)
    }

    private fun validateDetails(): Boolean {
        var isValid = true


        if (etName.length() == 0) {
            tName.error = getString(R.string.name_empty)
            isValid = false
        } else if (!validateName(etName.text.toString())) {
            tName.error = getString(R.string.name_invalid)
            isValid = false
        } else {
            tName.error = null
        }

        if (etUsername.length() == 0) {
            tUname.error = getString(R.string.username_empty)
            isValid = false
        } else if (!validateUsername(etUsername.text.toString())) {
            tUname.error = getString(R.string.username_invalid)
            isValid = false
        } else {
            tUname.error = null
        }


        if (etEmail.length() == 0) {
            temail.error = getString(R.string.email_empty)
            isValid = false
        } else if (!validateEmail(etEmail.text.toString())) {
            temail.error = getString(R.string.email_invalid)
            isValid = false
        } else {
            temail.error = null
        }



        if (etPassword.length() == 0) {
            tpassword.error = getString(R.string.pwd_empty)
            isValid = false
        } else if (etPassword.length() < 6) {
            tpassword.error = getString(R.string.pwd_length_min)
            isValid = false
        } else if (etPassword.length() > 20) {
            tpassword.error = getString(R.string.pwd_length_max)
            isValid = false
        } else if (!validatePassword(etPassword.text.toString())) {
            tpassword.error = getString(R.string.pwd_invalid)
            isValid = false
        } else {
            tilPassword.error = null
        }


        if (etPassword.text.toString() != etCnfPwd.text.toString()) {
            isValid = false
            tConf.error = getString(R.string.mismatch)
        } else {
            tConf.error = null
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

    private fun validatePassword(string: String): Boolean {

        val validity = string.checkPasswordSecurity(string)
        return validity


    }


    fun String.checkPasswordSecurity(str: String): Boolean {
        var containsSmallLetter = false
        var containsCapitalLetter = false
        var containsNumber = false

        for (c in str) {
            if (c in 'a'..'z') {
                containsSmallLetter = true
            }
            if (c in 'A'..'Z') {
                containsCapitalLetter = true
            }
            if (c in '0'..'9') {
                containsNumber = true
            }
        }


        return (containsSmallLetter && containsCapitalLetter && containsNumber)
    }


    private fun validateUsername(string: String): Boolean {
        for (c in string) {
            if (c !in 'A'..'Z' && c !in 'a'..'z' && c !in '0'..'9') {
                return false
            }
        }
        return true
    }

    private fun validateEmail(email: String): Boolean {
        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        val validator = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return validator.toRegex().matches(email)
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




