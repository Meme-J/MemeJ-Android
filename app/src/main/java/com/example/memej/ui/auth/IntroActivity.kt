package com.example.memej.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.memej.R
import com.example.memej.Utils.PreferenceUtil
import com.github.paolorotolo.appintro.AppIntro2
import com.github.paolorotolo.appintro.AppIntro2Fragment
import com.github.paolorotolo.appintro.model.SliderPage

class IntroActivity : AppIntro2() {


    private val preferenceUtil = PreferenceUtil
    //Plane Class extending the walkthrough library

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_intro)
        //Hide the status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        showStatusBar(false)
        setNavBarColor(R.color.stoneWhite)

        /**
         * Orderly list of what is to be done in intor screen
         *  HomeScreen with Showing how to complete memes of different people
         *  Swipeable of explore to show the explore
         *  Create a meme with a line
         *  Love, Share and spread the word
         */

        val intro = SliderPage(
            title = getString(R.string.intro),
            // imageDrawable = R.drawable.ic_fast_forward_black_24dp,
            titleColor = R.color.ivory,
            //descColor = R.color.stoneWhite,
            bgColor = R.color.colorAccent,
            imageDrawable = R.drawable.wk_intro

        )

        val page1 = SliderPage(
            title = getString(R.string.title1),
            description = getString(R.string.page1),
            titleColor = R.color.colorAccent,
            //descColor = R.color.stoneWhite,
            bgColor = R.color.ivory,
            imageDrawable = R.drawable.wk_home
        )

        val page2 = SliderPage(
            title = getString(R.string.title2),
            titleColor = R.color.ivory,
            // descColor = R.color.stoneWhite,
            bgColor = R.color.colorAccent,
            description = getString(R.string.page2),
            imageDrawable = R.drawable.wk_explore
        )
        val page3 = SliderPage(
            title = getString(R.string.title3),
            description = getString(R.string.page3),
            titleColor = R.color.colorAccent,
            //descColor = R.color.stoneWhite,
            bgColor = R.color.ivory,
            imageDrawable = R.drawable.wk_explore
        )

        val page4 = SliderPage(
            title = getString(R.string.title4),
            description = getString(R.string.page4),
            titleColor = R.color.ivory,
            //descColor = R.color.stoneWhite,
            bgColor = R.color.colorAccent,
            imageDrawable = R.drawable.wk_love
        )

        addSlide(AppIntro2Fragment.newInstance(intro))
        addSlide(AppIntro2Fragment.newInstance(page1))
        addSlide(AppIntro2Fragment.newInstance(page2))
        addSlide(AppIntro2Fragment.newInstance(page3))
        addSlide(AppIntro2Fragment.newInstance(page4))

        skipButtonEnabled = true
        setColorTransitionsEnabled(true)

        setImmersiveMode(true)

    }

    override fun onDonePressed(currentFragment: Fragment?) {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
        val preferences =
            getSharedPreferences(getString(R.string.intro_prefs), Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean(getString(R.string.intro_prefs_first_run), false)
        editor.apply()
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        startActivity(Intent(this, LoginActivity::class.java))
        finish()

        val preferences =
            getSharedPreferences(getString(R.string.intro_prefs), Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean(getString(R.string.intro_prefs_first_run), false)
        editor.apply()
    }


}
