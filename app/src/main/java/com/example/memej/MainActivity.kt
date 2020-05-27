package com.example.memej

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.Utils.Communicator
import com.example.memej.Utils.SessionManager
import com.example.memej.adapters.SearchAdapter
import com.example.memej.ui.MemeWorld.CompletedMemeActivity
import com.example.memej.ui.MemeWorld.MemeWorldFragment
import com.example.memej.ui.explore.ExploreFragment
import com.example.memej.ui.home.EditMemeContainerFragment
import com.example.memej.ui.home.HomeFragment
import com.example.memej.ui.home.SettingsScreen
import com.example.memej.ui.myMemes.MyMemesFragment
import com.example.memej.ui.profile.LikedMemes
import com.example.memej.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), Communicator {

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
                R.id.navigation_profile -> {
                    openFragment(ProfileFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }


    lateinit var sessionManager: SessionManager
    lateinit var rv: RecyclerView
    lateinit var adapter: SearchAdapter
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_explore,
                R.id.navigation_memeWorld,
                R.id.navigation_myMemes,
                R.id.navigation_profile
            )
        )
        //nav controller
        navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

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
//        val fab: FloatingActionButton = findViewById(R.id.fab_add)
//        fab.setOnClickListener {
//            val i = Intent(this, SelectMemeTemplateActivity::class.java)
//            startActivity(i)
//
//        }

        //Top Settings Intent
//        val btn_settings = findViewById<ShapeableImageView>(R.id.settings_btn)
//        btn_settings.setOnClickListener {
//            val i = Intent(this, SettingsScreen::class.java)
//            startActivity(i)
//        }

        //Function for passing data intent
        val frag = HomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container, frag).commit()

        //Implement search View


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_options_menu, menu)
        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu!!.findItem(R.id.navigation_search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        val searchView = menu.findItem(R.id.navigation_search)
        val settings = menu.findItem(R.id.settings_btn)
        settings.onNavDestinationSelected(navController)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i = Intent(this, SettingsScreen::class.java)

        when (item.itemId) {
            R.id.settings_btn -> startActivity(i)
            else ->
                return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun passDataFromHome(bundle: Bundle) {

        val transaction = this.supportFragmentManager.beginTransaction()
        val frag2 = EditMemeContainerFragment()
        frag2.arguments = bundle

        transaction.replace(R.id.container, frag2)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }

    override fun passDataToMemeWorld(bundle: Bundle) {
        val transaction = this.supportFragmentManager.beginTransaction()
        val frag2 = CompletedMemeActivity()
        frag2.arguments = bundle

        transaction.replace(R.id.container, frag2)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()

    }

    override fun goToLikedMemesPage() {
        val transaction = this.supportFragmentManager.beginTransaction()
        val frag2 = LikedMemes()
        transaction.replace(R.id.container, frag2)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }

}
