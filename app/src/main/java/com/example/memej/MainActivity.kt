package com.example.memej

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.example.memej.ui.MemeWorld.MemeWorldFragment
import com.example.memej.ui.MyDrafts.MyDraftsFragment
import com.example.memej.ui.explore.ExploreFragment
import com.example.memej.ui.home.HomeFragment
import com.example.memej.ui.memeTemplate.SelectMemeTemplateActivity
import com.example.memej.ui.myMemes.MyMemesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    //Get the res id
    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    openFragment(HomeFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_explore -> {
                    openFragment(ExploreFragment())

                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_memeWorld -> {
                    openFragment(MemeWorldFragment())

                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_myMemes -> {
                    openFragment(MyMemesFragment())

                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_myDrafts -> {
                    openFragment(MyDraftsFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        //   val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_explore,
                R.id.navigation_memeWorld,
                R.id.navigation_myMemes,
                R.id.navigation_myDrafts
            )
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        //navView.setupWithNavController(navController)

        //For opening the first time
        //Default
        if (savedInstanceState == null) {
            val fragment = HomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                .commit()
        }


        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


        //OnClickListener On FAB
        val fab: FloatingActionButton = findViewById(R.id.fab_add)
        fab.setOnClickListener {
            val i = Intent(this, SelectMemeTemplateActivity::class.java)
            startActivity(i)

        }

    }




}
