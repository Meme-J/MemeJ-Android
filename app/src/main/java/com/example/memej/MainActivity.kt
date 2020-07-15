package com.example.memej

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CursorAdapter
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import androidx.activity.viewModels
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
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
import com.example.memej.responses.SearchResponse
import com.example.memej.ui.MemeWorld.MemeWorldFragment
import com.example.memej.ui.auth.LoginActivity
import com.example.memej.ui.explore.ExploreFragment
import com.example.memej.ui.home.HomeFragment
import com.example.memej.ui.home.SearchResultActivity
import com.example.memej.ui.memeTemplate.SelectMemeTemplateActivity
import com.example.memej.ui.myMemes.MyMemesFragment
import com.example.memej.ui.profile.ProfileFragment
import com.example.memej.viewModels.MainActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shreyaspatil.MaterialDialog.MaterialDialog
import retrofit2.Call
import retrofit2.Response


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

    //    lateinit var navController: NavController
    lateinit var adapter: SearchAdapter

    var index = 0           //Default for HOme (Ongoing Memes)
    lateinit var searchView: SearchView
    lateinit var swl: SwipeRefreshLayout

    //Initialzie the toolbar
    lateinit var toolbar: androidx.appcompat.widget.Toolbar

    private val preferenceUtils = PreferenceUtil

    private val viewModel: MainActivityViewModel by viewModels()

    private var mAdapter: SimpleCursorAdapter? = null

    var searchType: String = ""
    private val preferenceManager: PreferenceManager = PreferenceManager()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        toolbar = androidx.appcompat.widget.Toolbar(this)
        toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        toolbar.title = ""


        val from = arrayOf("suggestionList")
        val to = intArrayOf(android.R.id.text1)

        mAdapter = SimpleCursorAdapter(
            this,
            android.R.layout.simple_list_item_1,
//           R.layout.list_suggestionn,
            null,
            from,
            to,
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )

        searchView = findViewById<SearchView>(R.id.activityCatalogSearch)
        searchView.suggestionsAdapter = mAdapter
        searchView.isIconifiedByDefault = false
        searchView.setBackgroundColor(resources.getColor(R.color.garbinaoGrey))

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


        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        sessionManager =
            SessionManager(this)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_explore,
//                R.id.navigation_home,
//                R.id.navigation_memeWorld,
//                R.id.navigation_myMemes,
//                R.id.navigation_profile
//            )
//        )


        val fragOpen = ExploreFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container, fragOpen).commit()
        navView.selectedItemId = R.id.exploreFragment


        //Default Fragment

        if (savedInstanceState == null) {
            val frag = ExploreFragment()
            supportFragmentManager.beginTransaction().replace(R.id.container, frag).commit()
            navView.selectedItemId = R.id.exploreFragment
        }

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


        //        navView.setOnNavigationItemReselectedListener {
        //            //Do nothing
        //        }

        //OnClickListener On FAB
        val fab: FloatingActionButton = findViewById(R.id.fab_add)
        //Sett attr
        fab.setBackgroundColor(resources.getColor(R.color.colorAccent))


        fab.setOnClickListener {
            val i = Intent(this, SelectMemeTemplateActivity::class.java)
            startActivity(i)

        }




        Log.e("Saved", savedInstanceState.toString())
        //Retreat back to home if not null
//        if (savedInstanceState != null) {
//            val frag = HomeFragment()
//            supportFragmentManager.beginTransaction().replace(R.id.container, frag).commit()
//            navView.selectedItemId = R.id.homeFragment
//        }


        //Function for passing data intent
        val frag = ExploreFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container, frag).commit()


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
//        val i = Intent(this, SettingsScreen::class.java)
        //Get index frag

        when (item.itemId) {
            R.id.settings_btn ->
                //startActivity(i)
                //Dialog of logout
                logout()

            //    R.id.navigation_search -> openSearch(Searchable(searchView), frag)

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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()

    }


}

