package com.example.memej.Utils

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

/**
 * Application Class
 */
class ApplicationUtil : Application() {

    companion object {

        lateinit var instance: ApplicationUtil


        fun getApplication(): ApplicationUtil {
            return instance
        }

        fun getContext(): Context {
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)


    }
}
