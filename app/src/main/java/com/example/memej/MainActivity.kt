package com.example.memej

import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.CursorAdapter
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.memej.Utils.Communicator
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.PreferenceUtil
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
import com.example.memej.ui.home.SettingsScreen
import com.example.memej.ui.memeTemplate.SelectMemeTemplateActivity
import com.example.memej.ui.memes.MemeByTag
import com.example.memej.ui.myMemes.MyMemesFragment
import com.example.memej.ui.profile.ProfileFragment
import com.example.memej.viewModels.MainActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.miguelcatalan.materialsearchview.MaterialSearchView
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

    private fun openSearch(
        fragment: Fragment,
        frag: Fragment?
    ) {

        val backstackedVerse = frag?.javaClass?.simpleName

        val manager = supportFragmentManager
        val fragPopped = manager.popBackStackImmediate(backstackedVerse, 0)

        if (!fragPopped) {
            val transaction = manager.beginTransaction()

            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
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
                    index = 0
                    openFragment(HomeFragment())
                    return@OnNavigationItemSelectedListener true

                }
                R.id.navigation_explore -> {
                    index = 1
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
    lateinit var navController: NavController
    lateinit var adapter: SearchAdapter

    var index = 0           //Default for HOme (Ongoing Memes)
    lateinit var searchView: SearchView
    lateinit var swl: SwipeRefreshLayout

    //Initialzie the toolbar
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private var atMainActicty = true

    private val preferenceUtils = PreferenceUtil
    lateinit var stringAdapter: ArrayAdapter<String>
    lateinit var mutableList: MutableList<String>

    lateinit var srv: MaterialSearchView
    private val viewModel: MainActivityViewModel by viewModels()

    private var mAdapter: SimpleCursorAdapter? = null

    var searchType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Set up the toolbar
        toolbar = androidx.appcompat.widget.Toolbar(this)
        toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)


        //val searchType = getIndexStringType(MainActivity().index)

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

        searchView = toolbar.findViewById(R.id.activityCatalogSearch) as SearchView
        searchView.suggestionsAdapter = mAdapter
        searchView.isIconifiedByDefault = false

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


        //        srv = toolbar.findViewById(R.id.search_view_lib)
        //        srv.setCursorDrawable(R.drawable.custom_cursor)
        //
        //        srv.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
        //            override fun onQueryTextSubmit(query: String): Boolean {
        //                //Do some magic
        //                return false
        //            }
        //
        //            override fun onQueryTextChange(newText: String): Boolean {
        //                val body = searchBody(newText, searchType.toString())
        //                Log.e("Body", body.toString() + " index" + MainActivity().index)
        //                fetchSuggestions(body)
        //                return true
        //            }
        //        })
        //
        //        srv.setOnSearchViewListener(object : SearchViewListener {
        //            override fun onSearchViewShown() {
        //
        //                //Do some magic
        //            }
        //
        //            override fun onSearchViewClosed() {
        //                //Do some magic
        //            }
        //        })
        //


        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        sessionManager =
            SessionManager(this)
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


        //Default Fragment
        if (savedInstanceState == null) {
            val fragment = HomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                .commit()
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


        //Function for passing data intent
        val frag = HomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container, frag).commit()


    }


    fun getTypeFromCurrentFragmnet(fragment: String?): String {

        var type: String = ""
        when (fragment) {
            "HomeFragment" -> type = "ongoing"
            "ExploreFragment" -> type = "ongoing"
            "MemeWorldFragment" -> type = "complete"
            "MyMemesFragment" -> type = "ongoing"
            "ProfileFragment" -> type = "complete"

        }

        return type

    }

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
        // Associate searchable configuration with the SearchView

        //Inflate Menu
        //val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        //searchView = menu!!.findItem(R.id.navigation_search).actionView as SearchView

        Log.e("SearchX", "In Inflate menu")

        //searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        //searchView.isIconifiedByDefault = false
        //searchView.queryHint = "Search"
        //searchView.requestFocus()
        // searchView.setBackgroundColor(resources.getColor(R.color.stoneWhite))

//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                fetchSuggestionsFromSearch(newText.toString())
//                return true
//            }
//        })


//        val frag = getFragmnetFromIndex(index)
//        Log.e("SearchX", "In On Create Options, before close")
//        searchManager.setOnCancelListener {
//            Log.e("SearchXM", "Close search")
//
//            openFragment(frag!!)
//        }
//        searchView.setOnCloseListener(object : SearchView.OnCloseListener {
//            override fun onClose(): Boolean {
//
//                Log.e("SearchX", "In overrriden method of close")
//                openFragment(frag!!)
//
//                return true
//            }
//        })

        //      val item = menu?.findItem(R.id.action_search)
//        srv.setMenuItem(item)

        return true

    }

//                    val columns = arrayOf(
//                        BaseColumns._ID,
//                        SearchManager.SUGGEST_COLUMN_TEXT_1,
//                        SearchManager.SUGGEST_COLUMN_INTENT_DATA
//                    )
//
//                    val cursor = MatrixCursor(columns)
//
//                    for (i in 0 until str.size - 1) {
//                        val tmp = arrayOf(
//                            Integer.toString(i),
//                            str.get(i),
//                            "COLUMNT_INTENT_DATA"
//                        )
//                        cursor.addRow(tmp)
//                    }
//                    Log.e("Cursor", cursor.toString())


    private fun getIndexStringType(index: Int): String? {

        var s: String? = null
        when (index) {
            0 -> s = "ongoing"
            1 -> s = "ongoing"
            2 -> s = "complete"
            3 -> s = "ongoing"
            4 -> s = "complete"

        }
        return s
    }

    private fun getFragmnetFromIndex(index: Int): Fragment? {
        var s: Fragment? = null
        when (index) {
            0 -> s = HomeFragment()
            1 -> s = ExploreFragment()
            2 -> s = MemeWorldFragment()
            3 -> s = MyMemesFragment()
            4 -> s = ProfileFragment()

        }
        return s

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i = Intent(this, SettingsScreen::class.java)
        //Get index frag
        val frag = getFragmnetFromIndex(index)

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


        //Remove the saved profile
        preferenceUtils.clearPrefData()
        val i = Intent(this, LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        finishAffinity()
        startActivity(i)

    }


    //Communicator Classes
    override fun passDataFromHome(bundle: Bundle) {

    }

    override fun passDataToMemeWorld(bundle: Bundle) {

    }

    override fun goToLikedMemesPage() {
    }

    override fun goToMemesByTagPage(bundle: Bundle) {
        val transaction = this.supportFragmentManager.beginTransaction()
        val frag2 = MemeByTag()

        frag2.arguments = bundle
        transaction.replace(R.id.container, frag2)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()

    }

    override fun goBackToHomePage() {
        val transaction = this.supportFragmentManager.beginTransaction()
        val frag2 = HomeFragment()
        transaction.replace(R.id.container, frag2)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }

    override fun goToSearchResult(bundle: Bundle) {


    }

    override fun goToProfilePage() {
        val transaction = this.supportFragmentManager.beginTransaction()
        val frag2 = ProfileFragment()
        transaction.replace(R.id.container, frag2)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()

    }

    override fun openLikedMemeFromActivity(bundle: Bundle) {

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


}

