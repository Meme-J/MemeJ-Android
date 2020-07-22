package com.example.memej.ui.drawerItems

import android.app.ProgressDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.memej.R
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.ui.memeTemplate.SearchTemplateActivty
import com.example.memej.viewModels.ExploreSpacesViewModel


class ExploreSpacesFragment : Fragment() {

    companion object {
        fun newInstance() = ExploreSpacesFragment()
    }

    private val viewModel: ExploreSpacesViewModel by viewModels()
    lateinit var sessionManager: SessionManager
    lateinit var bundle: Bundle
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var dialog: ProgressDialog
    lateinit var searchView: SearchView
    private var mAdapter: SimpleCursorAdapter? = null
    lateinit var searchManager: SearchManager
    lateinit var root: View
    lateinit var ctx: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.explore_spaces_fragment, container, false)
        ctx = ApplicationUtil.getContext()
        toolbar = root.findViewById(R.id.toolbar_drawer_element)
        toolbar.title = "Explore Spaces"
        sessionManager =
            SessionManager(ctx)


        initSearch()

        //SWL
        val swl = root.findViewById<SwipeRefreshLayout>(R.id.swl_explore_workspaces)
        swl.setOnRefreshListener {
            observeLiveData()
            swl.isRefreshing = false
        }
        val from = arrayOf("suggestionList")
        val to = intArrayOf(android.R.id.text1)

        //Add a searchManager
        searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        searchView.setSearchableInfo(searchManager.getSearchableInfo())


        mAdapter = SimpleCursorAdapter(
            ctx,
            android.R.layout.simple_list_item_1,
            null,
            from,
            to,
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )

        searchView.suggestionsAdapter = mAdapter
        searchView.isIconifiedByDefault = false

        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false        //Was true
            }

            override fun onSuggestionClick(position: Int): Boolean {

                val cursor: Cursor = mAdapter!!.getItem(position) as Cursor
                val txt = cursor.getString(cursor.getColumnIndex("suggestionList"))

                val bundle = bundleOf(
                    "tag" to txt
                )


                val i = Intent(requireContext(), SearchTemplateActivty::class.java)
                i.putExtra("tag", txt)
                i.putExtra("bundle", bundle)
                i.action = Intent.ACTION_SEARCH
                startActivity(i)


                return true


            }
        })


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //Returns query
                //Do nothing here

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                fetchSuggestions(newText.toString())
                return false
            }
        })




        if (ErrorStatesResponse.checkIsNetworkConnected(ctx)) {
            observeLiveData()

        } else {
            checkConnection()
        }
//        dialog.dismiss()


        return root
    }

    private fun checkConnection() {

    }


    private fun observeLiveData() {

//        if (!ErrorStatesResponse.checkIsNetworkConnected(this)) {
//            checkConnection()
//        }
//
//        viewModel.getPosts(pb = pb).observe(this, Observer {
//            memeGroupAdapter.submitList(it)
//        })
//
//
//        initializeList()

    }


    private fun initializeList() {
//        rv.layoutManager = GridLayoutManager(ctx, 2)
//        rv.adapter = memeGroupAdapter
//        dialog.dismiss()


    }


    private fun fetchSuggestions(toString: String) {

    }

    private fun initSearch() {
        //Get search View
        searchView = root.findViewById(R.id.activityWorkspaceSearch)
        //Hide the search mag
        val magId = resources.getIdentifier("android:id/search_mag_icon", null, null)
        val magImage = searchView.findViewById<View>(magId) as ImageView
        magImage.layoutParams = LinearLayout.LayoutParams(0, 0)

        //Hide base line
        val baseId = resources.getIdentifier("android:id/search_plate", null, null)
        val baseImage = searchView.findViewById<View>(baseId) as View
        baseImage.setBackgroundColor(resources.getColor(R.color.stoneWhite))

        magImage.setImageDrawable(null)

    }


}
