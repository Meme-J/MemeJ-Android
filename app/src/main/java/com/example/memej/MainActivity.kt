package com.example.memej

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.os.Handler
import android.provider.BaseColumns
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.memej.Utils.Communicator
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.PreferenceUtil
import com.example.memej.Utils.sessionManagers.PreferenceManager
import com.example.memej.Utils.sessionManagers.SaveSharedPreference
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.adapters.SearchAdapter
import com.example.memej.adapters.onClickSearch
import com.example.memej.entities.searchBody
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.ProfileResponse
import com.example.memej.responses.SearchResponse
import com.example.memej.ui.MemeWorld.MemeWorldFragment
import com.example.memej.ui.auth.LoginActivity
import com.example.memej.ui.drawerItems.ExploreSpacesFragment
import com.example.memej.ui.explore.ExploreFragment
import com.example.memej.ui.home.HomeFragment
import com.example.memej.ui.home.SearchResultActivity
import com.example.memej.ui.memeTemplate.SelectMemeTemplateActivity
import com.example.memej.ui.myMemes.MyMemesFragment
import com.example.memej.ui.profile.ProfileFragment
import com.example.memej.viewModels.MainActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.InstallStatus
import com.shreyaspatil.MaterialDialog.MaterialDialog
import io.reactivex.annotations.Nullable
import retrofit2.Call
import retrofit2.Response
import java.util.*


class MainActivity : AppCompatActivity(), Communicator, onClickSearch {

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    fun findFragment(): String? {
        val fm = supportFragmentManager.findFragmentById(R.id.container)
        val fragmentName: String = fm!!::class.java.simpleName
        return fragmentName
    }


    private val mOnNavDrawerItemSelectedLister =
        NavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.nav_drawer_explore_spaces -> {
                    openFragment(ExploreSpacesFragment())
                    drawer.closeDrawer(Gravity.LEFT)
                    return@OnNavigationItemSelectedListener true

                }


            }
            false
        }

    //Get the res id
    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {


                R.id.navigation_home -> {
                    index = 1
                    openFragment(HomeFragment())
                    return@OnNavigationItemSelectedListener true

                }
                R.id.navigation_explore -> {
                    index = 0
                    openFragment(ExploreFragment())

                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_memeWorld -> {
                    index = 2
                    openFragment(MemeWorldFragment())

                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_myMemes -> {
                    index = 3
                    openFragment(MyMemesFragment())

                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    index = 4
                    openFragment(ProfileFragment.newInstance())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }


    lateinit var sessionManager: SessionManager
    lateinit var rv: RecyclerView
    lateinit var adapter: SearchAdapter
    var index = 0           //Default for HOme (Ongoing Memes)
    lateinit var searchView: SearchView
    lateinit var swl: SwipeRefreshLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private val preferenceUtils = PreferenceUtil
    private val viewModel: MainActivityViewModel by viewModels()
    private var mAdapter: SimpleCursorAdapter? = null
    var searchType: String = ""
    private val preferenceManager: PreferenceManager = PreferenceManager()
    lateinit var navDrawerView: NavigationView
    lateinit var navView: BottomNavigationView
    var username = ""
    lateinit var fab: FloatingActionButton
    lateinit var drawer: DrawerLayout
    private var isInDrawerItem: Boolean = false
    lateinit var mAppBarConfiguration: AppBarConfiguration

    //App Update
    private var appUpdateManager: AppUpdateManager? = null
    private var RC_APP_UPDATE = 1249

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        toolbar = androidx.appcompat.widget.Toolbar(this)
        toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        sessionManager = SessionManager(this)
        //Set the Header of the navigation drawer

        //Initialize the navigation Drawer
        drawer = findViewById<DrawerLayout>(R.id.main_drawer__layout)
        mAppBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_drawer_explore_spaces,
            R.id.nav_drawer_my_spaces,
            R.id.nav_drawer_invites,
            R.id.nav_drawer_logout
        ).setDrawerLayout(drawer)
            .build()

        //Ref to navigation View
        navDrawerView = findViewById(R.id.nav_drawer_view)

        //Create a navigation Graph
        val navControllerDrawer =
            Navigation.findNavController(this, R.id.nav_host_fragment)
//        NavigationUI.setupActionBarWithNavController(
//            this,
//            navControllerDrawer,
//            mAppBarConfiguration
//        )

        NavigationUI.setupWithNavController(navDrawerView, navControllerDrawer)

        //OpenDrawer from button
        val imenu = findViewById<ImageView>(R.id.nav_menu_icon)
        imenu.setOnClickListener {
            drawer.openDrawer(Gravity.LEFT)

        }
        populateHeaderOfDrawer()

        val from = arrayOf("suggestionList")
        val to = intArrayOf(android.R.id.text1)

        mAdapter = SimpleCursorAdapter(
            this,
            android.R.layout.simple_list_item_1,
            null,
            from,
            to,
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )

        searchView = findViewById<SearchView>(R.id.activityCatalogSearch)
        searchView.suggestionsAdapter = mAdapter
        searchView.isIconifiedByDefault = false
        searchView.setBackgroundColor(resources.getColor(R.color.garbinaoGrey))

        //Hide the search mag
        val magId = resources.getIdentifier("android:id/search_mag_icon", null, null)
        val magImage = searchView.findViewById<View>(magId) as ImageView
        magImage.layoutParams = LinearLayout.LayoutParams(0, 0)

        //Hide base line
        val baseId = resources.getIdentifier("android:id/search_plate", null, null)
        val baseImage = searchView.findViewById<View>(baseId) as View
        baseImage.setBackgroundColor(resources.getColor(R.color.stoneWhite))

        magImage.setImageDrawable(null)

        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return true
            }

            override fun onSuggestionClick(position: Int): Boolean {

                val cursor: Cursor = mAdapter!!.getItem(position) as Cursor
                val txt = cursor.getString(cursor.getColumnIndex("suggestionList"))

                val bundle = bundleOf(
                    "tag" to txt,
                    "type" to searchType
                )

                val i = Intent(this@MainActivity, SearchResultActivity::class.java)
                i.putExtra("tag", txt)
                i.putExtra("type", searchType)
                i.putExtra("bundle", bundle)
                startActivity(i)

                return true


            }
        })


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                fetchSuggestions(newText.toString())
                return false
            }
        })


        //Close searchView
        searchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {


                hideKeyboard(this@MainActivity)
                searchView.clearFocus()
                searchView.isIconified = true

                return false
            }
        })


        navView = findViewById(R.id.nav_view)
        sessionManager =
            SessionManager(this)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_explore,
                R.id.navigation_home,
                R.id.navigation_memeWorld,
                R.id.navigation_myMemes,
                R.id.navigation_profile
            )
        )


        val fragOpen = ExploreFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container, fragOpen).commit()
        navView.selectedItemId = R.id.exploreFragment


        if (savedInstanceState == null) {
            val frag = ExploreFragment()
            supportFragmentManager.beginTransaction().replace(R.id.container, frag).commit()
            navView.selectedItemId = R.id.exploreFragment
        }

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navDrawerView.setNavigationItemSelectedListener(mOnNavDrawerItemSelectedLister)

        //Add Destination Item elected


        navControllerDrawer.addOnDestinationChangedListener { _, destination, _ ->


            Log.e("I am in where", destination.id.toString())
            when (destination.id) {
                R.id.exploreSpacesFragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }


        fab = findViewById(R.id.fab_add)
        fab.setBackgroundColor(resources.getColor(R.color.colorAccent))
        fab.setOnClickListener {
            val i = Intent(this, SelectMemeTemplateActivity::class.java)
            startActivity(i)

        }

//        if (!isInDrawerItem) {
//            showItems()
//        } else if (isInDrawerItem) {
//            hideItems()
//        }

        val frag = ExploreFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container, frag).commit()


    }

    private fun hideBottomNav() {
        navView.visibility = View.GONE
    }

    private fun showBottomNav() {
        navView.visibility = View.VISIBLE
    }


    private fun populateHeaderOfDrawer() {
        if (preferenceUtils.username == "") {

            //Check connection here
            if (ErrorStatesResponse.checkIsNetworkConnected(this)) {
                callUser()
            }
        } else {

            username = preferenceUtils.getUserFromPrefernece().username

        }

        //After this, we will have the username
        //Get the ref for the items of the nav drawer
        val v: View = navDrawerView.getHeaderView(0)
        username = preferenceUtils.getUserFromPrefernece().username

        //Check the presence of the View
        (v.findViewById<View>(R.id.nav_header_username) as TextView).text = username

        val x = username.take(2).toUpperCase(Locale.ROOT)
        val c = (v.findViewById<View>(R.id.nav_header_image_view) as CardView)
        c.setCardBackgroundColor(resources.getColor(R.color.colorPrimary))
        val t = v.findViewById<View>(R.id.nav_header_avatar_text) as TextView
        t.text = x

    }

    private fun callUser() {
        val service = RetrofitClient.getAuthInstance()

        service.getUser(accessToken = "Bearer ${sessionManager.fetchAcessToken()}")
            .enqueue(object : retrofit2.Callback<ProfileResponse> {
                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {

                    if (response.isSuccessful) {

                        username = response.body()?.profile?.username!!
                        setPreferencesUser(response.body()?.profile!!)
                    }
                }
            })

    }

    private fun setPreferencesUser(user: ProfileResponse.Profile) {
        preferenceUtils.setUserFromPreference(user)
    }


    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view: View? = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getTypeFromCurrentFragmnet(fragment: String?): String {

        var type: String = ""
        when (fragment) {
            "ExploreFragment" -> type = "ongoing"
            "HomeFragment" -> type = "ongoing"
            "MemeWorldFragment" -> type = "complete"
            "MyMemesFragment" -> type = "ongoing"
            "ProfileFragment" -> type = "complete"

        }

        return type

    }


    @Keep
    private fun fetchSuggestions(str: String) {

        val frag = findFragment()
        val type = getTypeFromCurrentFragmnet(frag)
        searchType = type
        Log.e("Frag", frag.toString())
        val body = searchBody(str, type)
        val memeService = RetrofitClient.makeCallsForMemes(this)
        memeService.getSuggestions(
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}",
            info = body
        ).enqueue(object : retrofit2.Callback<SearchResponse> {
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {

                val message = ErrorStatesResponse.returnStateMessageForThrowable(t)

            }

            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {

                if (response.isSuccessful) {
                    val str = mutableListOf<String>()
                    for (y: SearchResponse.Suggestion in response.body()!!.suggestions) {
                        str.add(y.tag)
                    }

                    Log.e("STr", str.toString())

                    val c =
                        MatrixCursor(arrayOf(BaseColumns._ID, "suggestionList"))
                    for (i in 0 until str.size) {
                        c.addRow(arrayOf(i, str[i]))
                    }

                    mAdapter?.changeCursor(c)
                }


            }
        })


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_options_menu, menu)

        return true

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.settings_btn ->
                logout()


            else ->
                return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun logout() {
        //Ask the user
        val mDialog = MaterialDialog.Builder(this)
            .setTitle("Logout?")
            .setMessage("Are you sure want to logout?")
            .setCancelable(true)
            .setPositiveButton(
                "Yes",
                R.drawable.ic_exit_to_app_black_24dp
            ) { dialogInterface, which ->
                logoutDo()
            }
            .setNegativeButton(
                "No"
            ) { dialogInterface, which -> dialogInterface.dismiss() }
            .build()
        mDialog.show()

    }

    private fun logoutDo() {

        SaveSharedPreference()
            .setLoggedIn(applicationContext, false)
        preferenceUtils.clearPrefData()
        preferenceManager.clear()

        val i = Intent(this, LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        finishAffinity()
        startActivity(i)
        finish()
    }


    override fun goToAFragmnet(fragment: Fragment) {
        val transaction = this.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()

    }


    override fun getSuggestion(_sug: SearchResponse.Suggestion) {

    }


    //App Update

    private fun updateApp() {

        appUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateManager!!.registerListener(installStateUpdatedListener)

    }

    //Install State Listener
    var installStateUpdatedListener: InstallStateUpdatedListener? =
        object : InstallStateUpdatedListener {
            override fun onStateUpdate(state: InstallState) {
                if (state.installStatus() === InstallStatus.DOWNLOADED) {
                    //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                    popupSnackbarForCompleteUpdate()
                } else if (state.installStatus() === InstallStatus.INSTALLED) {
                    appUpdateManager?.unregisterListener(this)
                } else {
                    Log.i(
                        "Activity",
                        "InstallStateUpdatedListener: state: " + state.installStatus()
                    )
                }
            }
        }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        @Nullable data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != Activity.RESULT_OK) {
                Log.e("Androud", "onActivityResult: app download failed")
            }
        }
    }

    //PopUpView
    private fun popupSnackbarForCompleteUpdate(): Unit {
        val snackbar: Snackbar = Snackbar.make(
            findViewById(R.id.main_drawer__layout),
            "App has been updated",
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction("Install") { view ->
            appUpdateManager?.completeUpdate()
        }
        snackbar.setActionTextColor(resources.getColor(R.color.wildColor))
        snackbar.show()
    }


    //States
    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ updateApp() }, 1000)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController =
            Navigation.findNavController(this, R.id.nav_host_fragment)
        return (NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp())
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.main_drawer__layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onStop() {
        appUpdateManager?.unregisterListener(installStateUpdatedListener)
        super.onStop()
    }


}

