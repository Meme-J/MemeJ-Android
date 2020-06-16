package com.example.memej.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.memej.MainActivity
import com.example.memej.R
import com.example.memej.Utils.Communicator
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.adapters.SearchAdapter
import com.example.memej.adapters.onClickSearch
import com.example.memej.entities.searchBody
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.SearchResponse
import com.example.memej.ui.MemeWorld.MemeWorldFragment
import com.example.memej.ui.explore.ExploreFragment
import com.example.memej.ui.myMemes.MyMemesFragment
import com.example.memej.ui.profile.ProfileFragment
import retrofit2.Call
import retrofit2.Response


class Searchable(val searchView: SearchView) : Fragment(), onClickSearch {

    lateinit var rv: RecyclerView
    lateinit var adapter: SearchAdapter
    lateinit var sessionManager: SessionManager
    lateinit var comm: Communicator
    lateinit var pb: ProgressBar
    lateinit var tv: TextView
    lateinit var searchType: String
    lateinit var lav: LottieAnimationView
    private var originalMode: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.activity_searchable, container, false)

        //Get the rv and adapter
        sessionManager =
            SessionManager(requireContext())
        comm = activity as Communicator
        rv = root.findViewById(R.id.rv_suggestions_frame)
        adapter = SearchAdapter(this)
        rv.layoutManager = LinearLayoutManager(context)
        pb = root.findViewById(R.id.pb_search)
        Log.e("Searchable", "In Empty Searchable")
        tv = root.findViewById(R.id.empty_tv)
        lav = root.findViewById(R.id.searchAnimation)
        val tsv = root.findViewById<TextView>(R.id.tvTTS)
        //Open the input medthod


        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
        )

        showKeyboard(context)




        Log.e("Searchable", "Before query change")
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.e("Search", "In query submit")
                lav.visibility = View.GONE
                tsv.visibility = View.GONE
                fetchSuggestions(query, searchView)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.e("Search", "In query change")
                lav.visibility = View.GONE

                tsv.visibility = View.GONE
                fetchSuggestions(newText, searchView)
                return false
            }
        })


        //Closing view
        searchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {

                Log.e("Searchable", "In overrriden method of close within Searchable")
                //Get index and fragment
                val fragment = getFragmnetFromIndex(MainActivity().index)
                //Close the input keboard
                hideKeyboard(context)
                comm.goToAFragmnet(fragment!!)

                return true
            }
        })

        searchView.invalidate()


        //Acticty instance
//        val activity = activity as MainActivity?
//
//        val toolbar = activity?.toolbar
////        val toolbar = (getActivity() as AppCompatActivity?)!!.supportActionBar
//        //Get the toolbar from Main Activity
//
//        toolbar!!.setNavigationOnClickListener(View.OnClickListener {
//            Toast.makeText(
//                activity,
//                "Back clicked!",
//                Toast.LENGTH_SHORT
//            ).show()
//        })
        return root
    }

    private fun hideKeyboard(context: Context?) {

        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus
        if (view == null) {
            View(activity)
        }

        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun showKeyboard(context: Context?) {
        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        inputMethodManager.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )

    }


    private fun fetchSuggestions(query: String?, searchView: SearchView) {

        Log.e("Search", "In fetch")
        pb.visibility = View.VISIBLE

        searchType = getIndexStringType(MainActivity().index)

        val body = searchBody(query!!, searchType = searchType.toString())
        Log.e("Search", "Search Type is" + searchType.toString() + body.toString())


        val service = RetrofitClient.makeCallsForMemes(requireContext())
        service.getSuggestions(
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}",
            info = body
        ).enqueue(object : retrofit2.Callback<SearchResponse> {
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                Log.e("Search", "FAILED FETCH")
                pb.visibility = View.GONE

            }

            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                //Get the response
                Log.e("Search", "RESP FETCH")

                if (response.code() == 200) {
                    //Successfull suggestion
                    val suggestions: List<SearchResponse.Suggestion> = response.body()!!.suggestions
                    Log.e("Search", "Suggestions now" + suggestions.toString())

                    val str = mutableListOf<String>()
                    for (y: SearchResponse.Suggestion in response.body()!!.suggestions) {
                        str.add(y.tag)
                    }


                    if (suggestions.size != 0) {
                        adapter.searchItems = suggestions
                        rv.adapter = adapter
                        adapter.notifyDataSetChanged()
                        pb.visibility = View.GONE
                    } else {
                        val type = getIndexStringType(MainActivity().index)
                        tv.text = "Unable to get memes which are " + type.toString()
                        pb.visibility = View.GONE
                    }

                } else {
                    Toast.makeText(
                        context,
                        response.errorBody().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    pb.visibility = View.GONE
                }

            }
        })


    }

    private fun getIndexStringType(index: Int): String {

        var s: String = ""
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

    override fun getSuggestion(_sug: SearchResponse.Suggestion) {
        //use it as a search suggesstion
        //tHIS IS WHEN THE SUGGESTION IS CLICKED
        getMemeAndReplace(_sug.tag, searchType)

    }

    private fun getMemeAndReplace(tag: String, searchType: String) {

        val bundle = bundleOf(
            "tag" to tag,
            "type" to searchType
        )
        val i = Intent(context, SearchResultActivity::class.java)
        i.putExtra("tag", tag)
        i.putExtra("type", searchType)
        i.putExtra("bundle", bundle)
        startActivity(i)


    }


}