package com.example.memej.ui.auth

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.memej.MainActivity
import com.example.memej.R
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SaveSharedPreference
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.databinding.ActivityLoginBinding
import com.example.memej.entities.LoginBody
import com.example.memej.viewModels.LoginActivtyViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    //Instanec of Shared Preferences


    lateinit var etUsername: TextInputEditText
    lateinit var etPassword: TextInputEditText
    lateinit var t1: TextInputLayout
    lateinit var t2: TextInputLayout
    lateinit var pb: ProgressBar
    private lateinit var sessionManager: SessionManager


    private val viewModel: LoginActivtyViewModel by viewModels()
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        t1 = findViewById(R.id.tilUsername)
        t2 = findViewById(R.id.tilPassword)

        pb = findViewById(R.id.pb_login)
        sessionManager =
            SessionManager(this)

        t1.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (ErrorStatesResponse.checkIsNetworkConnected(this)) {
                    if (validateDetail()) {

                        login()
                    }
                }

            }
            false
        }

        viewModel.successful.observe(this, Observer { successful ->
            hideProgressBar()
            if (successful != null) {
                if (successful) {
                    Toast.makeText(this, R.string.successLogin, Toast.LENGTH_LONG)
                        .show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
                    hideKeyboard(this)
//                    val b = bundleOf(
//                        "frag" to "explore"
//                    )
//                    intent.putExtra("bundleMain", b)

                    startActivity(intent)
                    finish()
                } else {
                    Snackbar.make(signinView, viewModel.message, Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        })


        val b: MaterialButton = findViewById(R.id.login_btn)
        b.setOnClickListener {

            if (validateDetail()) {
                if (ErrorStatesResponse.checkIsNetworkConnected(this)) {
                    pb.visibility = View.VISIBLE
                    login()

                } else {
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

    }

    private fun hideKeyboard(activity: Activity) {
        val imm =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun hideProgressBar() {
        pb.visibility = View.GONE
    }

    private fun showProgressBar() {
        pb.visibility = View.VISIBLE
    }


    private fun login() {
        viewModel.login(
            LoginBody(
                etUsername.text.toString().trim(),
                etPassword.text.toString().trim()
            )
        )

    }


    private fun goToMainActivity() {

        pb.visibility = View.GONE

        SaveSharedPreference()
            .setLoggedIn(applicationContext, true)
        val i = Intent(this, MainActivity::class.java)
//        val b = bundleOf(
//            "frag" to "explore"
//        )
//        i.putExtra("bundleMain", b)

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(i)

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


    override fun onDestroy() {
        super.onDestroy()
        viewModel.successful.removeObservers(this)
        viewModel.successful.value = null
    }


}


