package com.example.memej.ui.memeTemplate

import android.app.ProgressDialog
import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.View
import android.widget.CursorAdapter
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.adapters.MemeGroupAdapter
import com.example.memej.adapters.OnItemClickListenerTemplate
import com.example.memej.entities.searchTemplate
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.SearchResponse
import com.example.memej.responses.template.EmptyTemplateResponse
import com.example.memej.viewModels.SelectMemeGroupViewModel
import com.shreyaspatil.MaterialDialog.MaterialDialog
import retrofit2.Call
import retrofit2.Response

class SelectMemeTemplateActivity : AppCompatActivity(), OnItemClickListenerTemplate {


    private lateinit var memeGroupAdapter: MemeGroupAdapter
    private lateinit var rv: RecyclerView
    private val viewModel: SelectMemeGroupViewModel by viewModels()
    lateinit var pb: ProgressBar
    lateinit var sessionManager: SessionManager
    lateinit var bundle: Bundle
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var dialog: ProgressDialog
    lateinit var searchView: SearchView
    private var mAdapter: SimpleCursorAdapter? = null

    override fun onItemClickedForTemplate(mg: EmptyTemplateResponse.Template) {

        //When we click a new template to be created

        bundle = bundleOf(

            "imageUrl" to mg.imageUrl,
            "id" to mg._id,
            "textColorCode" to mg.textColorCode,
            "textSize" to mg.textSize,
            "numPlaceholders" to mg.numPlaceholders,
            "coordinate" to mg.coordinates,
            "tags" to mg.tags,
            "name" to mg.name

        )


        val i = Intent(this, NewMemeContainer::class.java)
        i.putExtra("bundle", bundle)

        startActivity(i)
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_meme_template)

        //BAck NAvigation
        pb = findViewById(R.id.pb_template)
        pb.visibility = View.VISIBLE
        toolbar = findViewById(R.id.addMemeTb)
        sessionManager =
            SessionManager(this)

        rv = findViewById(R.id.rv_memeGroup)
        memeGroupAdapter = MemeGroupAdapter(this)
        dialog = ProgressDialog(this)
        dialog.setMessage("Loading templates...")
        dialog.show()

        //Get a dialog loader

        //Get search View
        searchView = findViewById(R.id.activityTemplateSearch)
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
                    "tag" to txt
                )


                val i = Intent(this@SelectMemeTemplateActivity, SearchTemplateActivty::class.java)
                i.putExtra("tag", txt)
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




        if (ErrorStatesResponse.checkIsNetworkConnected(this)) {
            observeLiveData()

        } else {
            checkConnection()
        }
        dialog.dismiss()

        pb.isIndeterminate = false
        pb.visibility = View.GONE

    }

    private fun fetchSuggestions(str: String) {

        //This is not yet made
        val memeService = RetrofitClient.makeCallsForMemes(this)
        val st = searchTemplate(str)
        memeService.getTemplateSuggestions(
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}",
            info = st
        ).enqueue(object : retrofit2.Callback<SearchResponse> {
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {

                val message = ErrorStatesResponse.returnStateMessageForThrowable(t)

            }

            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {

                if (response.isSuccessful) {
                    val strAr = mutableListOf<String>()
                    for (y: SearchResponse.Suggestion in response.body()!!.suggestions) {
                        strAr.add(y.tag)
                    }

                    Log.e("STr", strAr.toString())
                    Log.e("Str", strAr.size.toString())


                    val c =
                        MatrixCursor(arrayOf(BaseColumns._ID, "suggestionList"))
                    for (i in 0 until strAr.size) {
                        c.addRow(arrayOf(i, strAr[i]))
                    }


                    mAdapter?.changeCursor(c)
                }


            }
        })


    }

    private fun checkConnection() {
        pb.visibility = View.GONE
        dialog.dismiss()
        val mDialog = MaterialDialog.Builder(this)
            .setTitle("Oops")
            .setMessage("No internet connection")
            .setCancelable(true)
            .setAnimation(R.raw.inter2)
            .setPositiveButton(
                "Retry"
            ) { dialogInterface, which ->
                dialogInterface.dismiss()
                dialog.show()
                observeLiveData()
            }
            .setNegativeButton(
                "Cancel"
            ) { dialogInterface, which ->
                dialogInterface.dismiss()
                pb.visibility = View.GONE

                pb.isIndeterminate = false
            }
            .build()
        mDialog.show()

    }

    private fun observeLiveData() {

        if (!ErrorStatesResponse.checkIsNetworkConnected(this)) {
            checkConnection()
        }

        viewModel.getPosts(pb = pb).observe(this, Observer {
            memeGroupAdapter.submitList(it)
        })


        initializeList()

    }


    private fun initializeList() {
        rv.layoutManager = GridLayoutManager(this, 2)
        rv.adapter = memeGroupAdapter
        //After this has been done, close the pb
        pb.visibility = View.GONE
        pb.isIndeterminate = false
        dialog.dismiss()
    }


}
