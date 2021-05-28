package com.example.memej.ui.memeTemplate

import android.app.ProgressDialog
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.adapters.MemeGroupAdapter
import com.example.memej.adapters.OnItemClickListenerTemplate
import com.example.memej.models.responses.template.EmptyTemplateResponse
import com.example.memej.viewModels.SearchTemplateViewModel
import com.shreyaspatil.MaterialDialog.MaterialDialog

class SearchTemplateActivty : AppCompatActivity(), OnItemClickListenerTemplate {


    private val viewModel: SearchTemplateViewModel by viewModels()
    var tagName: String? = ""              //Deafult Query
    var type: String? = ""           //Default

    lateinit var arg: Bundle
    lateinit var rv: RecyclerView
    lateinit var adapterComplete: MemeGroupAdapter
    lateinit var pb: ProgressBar
    lateinit var dialog: ProgressDialog
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    val VOICE_RECOGNITION_REQUEST_CODE = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_template_activty)

        val jargon: Boolean =
            intent.getBundleExtra(SearchManager.APP_DATA)?.getBoolean("JARGON") ?: false
        arg = intent?.getBundleExtra("bundle")!!
        toolbar = findViewById(R.id.tb_searchResultTemplate)
        pb = findViewById(R.id.pb_searchResultTemplate)
        tagName = arg.getString("tag")
        toolbar.title = tagName
        rv = findViewById(R.id.rv_memes_searchTemplate)

        Log.e("Arg", arg.toString())

        Log.e(
            "Arg",
            Intent.ACTION_SEARCH.toString() + intent.action.toString()
        )


        //Action search from manager
        if (Intent.ACTION_SEARCH == intent.action) {
            Log.e("In intent oka", "In intent okay")


//            val q = intent.getStringExtra(SearchManager.QUERY)

            Log.e("Query", tagName!!.toString())
            //Save the recent query
//            SearchRecentSuggestions(
//                this,
//
//                MySuggestionProvider.AUTHORITY,
//                MySuggestionProvider.MODE
//            )
//                .saveRecentQuery(q, null)

            //Set tagame

            if (ErrorStatesResponse.checkIsNetworkConnected(this)) {
                observeLiveData()

            } else {
                checkConnection()
            }


        }

        //Option to clear history
        Log.e("Arg", tagName!!)
//        SearchRecentSuggestions(this, MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)
//            .clearHistory()

    }

    private fun observeLiveData() {
        if (!ErrorStatesResponse.checkIsNetworkConnected(this)) {
            checkConnection()
        }

        adapterComplete = MemeGroupAdapter(this)
        viewModel.getPosts(tagName.toString()).observe(this, Observer {
            adapterComplete.submitList(it)
        })


        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapterComplete
        pb.visibility = View.GONE

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


    override fun onItemClickedForTemplate(mg: EmptyTemplateResponse.Template) {

        //When we click a new template to be created

        val bundle = bundleOf(

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


}
