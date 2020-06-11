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
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.memej.Utils.Communicator
import com.example.memej.Utils.SessionManager
import com.example.memej.adapters.SearchAdapter
import com.example.memej.adapters.onClickSearch
import com.example.memej.responses.SearchResponse
import com.example.memej.ui.MemeWorld.MemeWorldFragment
import com.example.memej.ui.explore.ExploreFragment
import com.example.memej.ui.home.HomeFragment
import com.example.memej.ui.home.Searchable
import com.example.memej.ui.home.SettingsScreen
import com.example.memej.ui.memeTemplate.SelectMemeTemplateActivity
import com.example.memej.ui.memes.MemeByTag
import com.example.memej.ui.myMemes.MyMemesFragment
import com.example.memej.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity(), Communicator, onClickSearch {

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Set up the toolbar
        toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)


        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        sessionManager = SessionManager(this)
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

//        val host = NavHostFragment.create(R.navigation.mobile_navigation)
        //     navController = findNavController(R.id.nav_host_fragment)
        //Setting up the main activity navigation

//        supportFragmentManager.beginTransaction().replace(R.id.container, host)
//            .setPrimaryNavigationFragment(host).commit()


        //Default Fragment
        if (savedInstanceState == null) {
            val fragment = HomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                .commit()
        }
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

//Initilaize th adapter of serach
//        adapter = SearchAdapter(this)
//        rv = findViewById(R.id.rv_suggestions)
//        rv.layoutManager = LinearLayoutManager(this)


        //OnClickListener On FAB
        val fab: FloatingActionButton = findViewById(R.id.fab_add)
        fab.setOnClickListener {
            val i = Intent(this, SelectMemeTemplateActivity::class.java)
            startActivity(i)

        }


        //Function for passing data intent
        val frag = HomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container, frag).commit()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_options_menu, menu)
        // Associate searchable configuration with the SearchView

        //Inflate Menu
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu!!.findItem(R.id.navigation_search).actionView as SearchView


        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.isIconifiedByDefault = false
        searchView.queryHint = "Search"
        searchView.requestFocus()

        searchManager.setOnCancelListener {
            val frag = getFragmnetFromIndex(index)
            openFragment(frag!!)
        }

        return true

//Cursor Formation
        // searchView.queryHint("Search")
//
//        val columNames =
//            arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
//        val viewIds = intArrayOf(android.R.id.text1)
//         adap= SimpleCursorAdapter(
//            this,
//            android.R.layout.simple_list_item_1, null, columNames, viewIds
//        )
//        searchView.setOnSuggestionListener(getOnSuggestionClickListener())


//        searchView.setOnQueryTextListener(getOnQueryTextListener(this, adapter))

    }


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

//
//    private fun getOnQueryTextListener(
//        mainActivity: MainActivity,
//        adapter: SearchAdapter
//    ): SearchView.OnQueryTextListener? {
//        return object : SearchView.OnQueryTextListener {
//
//            //Separate method for this
//            override fun onQueryTextSubmit(s: String): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(s: String): Boolean {
//                if (s.length < 1) {
//                    return false            //When it is not Able fetch
//                }
//
//                Log.e("Search", s)
//                val service = RetrofitClient.makeCallsForMemes(this@MainActivity)
//                val body = searchBody(s, "ongoing")
//                service.getSuggestions(
//                    accessToken = "Bearer " + sessionManager.fetchAcessToken(),
//                    info = body
//                ).enqueue(object : retrofit2.Callback<SearchResponse> {
//                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
//                        Log.e("Search", "Failded to load the serach suggestions")
//
//                    }
//
//                    override fun onResponse(
//                        call: Call<SearchResponse>,
//                        response: Response<SearchResponse>
//                    ) {
//                        Log.e("Searc ", "Resp" + response.body())
//                        adapter.setAdapterSearch(response.body()!!.suggestions)
//                        adapter.notifyDataSetChanged()
//                        rv.adapter = adapter
//                    }
//                })
//
//
//                return true
//            }
//        }
//    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i = Intent(this, SettingsScreen::class.java)

        when (item.itemId) {
            R.id.settings_btn -> startActivity(i)
            R.id.navigation_search -> openFragment(Searchable(searchView))

            else ->
                return super.onOptionsItemSelected(item)
        }
        return true
    }

    //Exit app
    override fun onBackPressed(): Unit {
        finish()
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


    override fun getSuggestion(_sug: SearchResponse.Suggestion) {

    }


}

