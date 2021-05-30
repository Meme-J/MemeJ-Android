package com.example.memej.Utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.View
import com.example.memej.R
import com.google.android.material.snackbar.Snackbar
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

object ErrorStatesResponse {


    fun returnStateMessageForThrowable(throwable: Throwable): String {
        var message: String = ""


        when (throwable) {
            is IOException -> {
                message = ApplicationUtil.getContext()
                    .getString(R.string.error_please_check_internet)
            }
            is TimeoutException -> {
                message = ApplicationUtil.getContext()
                    .getString(R.string.error_request_timed_out)
            }
            is HttpException -> {
                val httpException = throwable
                val response = httpException.response()?.errorBody()?.string()

                message = response!!
            }
            else -> {
                message = ApplicationUtil.getContext()
                    .getString(R.string.error_something_went_wrong)
            }
        }

        return message
    }

    fun checkIsNetworkConnected(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

    }

    fun logExceptions(e: Exception, tag: String) {
        Log.e(tag, "Exception with message and cause ${e.message} and ${e.cause}")
    }

    fun logThrowables(t: Throwable, tag: String) {
        Log.e(tag, "Throwabale with message and cause ${t.message} and ${t.cause}")
    }

    fun createSnackbar(message: String?, mSnackbar: View) {
        Snackbar.make(mSnackbar, message.toString(), Snackbar.LENGTH_SHORT).show()
    }


}