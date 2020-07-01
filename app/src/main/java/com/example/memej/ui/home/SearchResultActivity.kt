package com.example.memej.ui.home

import android.app.ProgressDialog
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
import com.example.memej.adapters.HomeMemeAdapter
import com.example.memej.adapters.MemeWorldAdapter
import com.example.memej.adapters.OnItemClickListenerHome
import com.example.memej.adapters.OnItemClickListenerMemeWorld
import com.example.memej.responses.homeMememResponses.Meme_Home
import com.example.memej.responses.memeWorldResponses.Meme_World
import com.example.memej.ui.MemeWorld.CompletedMemeActivity
import com.example.memej.viewModels.SearchResultActivityViewModel
import com.shreyaspatil.MaterialDialog.MaterialDialog


class SearchResultActivity : AppCompatActivity(), OnItemClickListenerHome,
    OnItemClickListenerMemeWorld {

    var tagName: String? = ""              //Deafult Query
    var type: String? = ""           //Default

    lateinit var arg: Bundle
    lateinit var rv: RecyclerView
    lateinit var adapterOnGoing: HomeMemeAdapter
    lateinit var adapterComplete: MemeWorldAdapter
    lateinit var pb: ProgressBar
    lateinit var dialog: ProgressDialog

    private val viewmodel: SearchResultActivityViewModel by viewModels()
    lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)


        arg = intent?.getBundleExtra("bundle")!!
        toolbar = findViewById(R.id.tb_searchResult)
        pb = findViewById(R.id.pb_searchResult)
        tagName = arg.getString("tag")
        type = arg.getString("type")
        toolbar.title = tagName

        //Navigate back
//        toolbar.setNavigationOnClickListener {
//            val i = Intent(this, MainActivity::class.java)
//            startActivity(i)
//            finish()
//        }


        pb.visibility = View.VISIBLE
        dialog = ProgressDialog(this)
        dialog.setMessage("Loading Memes...")
        dialog.show()

        rv = findViewById(R.id.rv_memes_search)

        if (ErrorStatesResponse.checkIsNetworkConnected(this)) {
            goThroughType()
        } else {
            checkConnection()
        }

        dialog.dismiss()
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
                goThroughType()
            }
            .setNegativeButton(
                "Cancel"
            ) { dialogInterface, which ->
                dialogInterface.dismiss()
                pb.visibility = View.GONE

            }
            .build()
        mDialog.show()


    }

    private fun goThroughType() {

        if (!ErrorStatesResponse.checkIsNetworkConnected(this)) {
            checkConnection()
        } else {
            dialog.show()
            if (type == "complete") {

                getCompleteMemes()
            } else {

                getOngoingMemes()
            }
        }
    }

    private fun getOngoingMemes() {

        adapterOnGoing = HomeMemeAdapter(this)

        viewmodel.getOnGoing(tagName.toString(), pb).observe(this, Observer {
            adapterOnGoing.submitList(it)
        })


        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapterOnGoing
        pb.visibility = View.GONE
        dialog.dismiss()
    }


    private fun getCompleteMemes() {

        adapterComplete = MemeWorldAdapter(this, this)
        viewmodel.getComplete(tagName.toString(), pb).observe(this, Observer {
            adapterComplete.submitList(it)
        })


        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapterComplete
        pb.visibility = View.GONE
        dialog.dismiss()

    }


    override fun onItemClicked(_meme: Meme_World) {
        //We are travelling in the same main actvity
        //Getting the image and everything
        val bundle = bundleOf(
            "id" to _meme._id,
            "lastUpdated" to _meme.lastUpdated,
            "likedBy" to _meme.likedBy,
            "likes" to _meme.likes,
            "placeholders" to _meme.placeholders,
            "numPlaceholders" to _meme.templateId.numPlaceholders,
            "tags" to _meme.tags,
            "users" to _meme.users,
            "templateIdCoordinates" to _meme.templateId.coordinates,
            "imageUrl" to _meme.templateId.imageUrl,
            "imageTags" to _meme.templateId.tags,
            "imageName" to _meme.templateId.name,
            "textSize" to _meme.templateId.textSize,
            "textColor" to _meme.templateId.textColorCode
        )

        Log.e(
            "Values of complete search",
            _meme._id + _meme.placeholders.toString() + _meme.tags.toString()
        )

        val i = Intent(this, CompletedMemeActivity::class.java)
        i.putExtra("bundle", bundle)
        startActivity(i)


    }


    override fun onItemClicked(_homeMeme: Meme_Home) {

        //Go to edit meme fragmnet container

        val bundle = bundleOf(
            "id" to _homeMeme._id,
            "lastUpdated" to _homeMeme.lastUpdated,
            "numPlaceHolders" to _homeMeme.numPlaceholders,
            "placeHolders" to _homeMeme.placeholders,
            "stage" to _homeMeme.stage,
            "tags" to _homeMeme.tags,
            "users" to _homeMeme.users,
            "paint" to _homeMeme.templateId.textColorCode,
            "size" to _homeMeme.templateId.textSize,
            "templateIdCoordinates" to _homeMeme.templateId.coordinates,
            "image" to _homeMeme.templateId.imageUrl,
            "imageUrl" to _homeMeme.templateId.imageUrl,
            "imageTags" to _homeMeme.templateId.tags,
            "imageName" to _homeMeme.templateId.name

        )

        //Go to the edit link
        val i = Intent(this, EditMemeContainerFragment::class.java)
        i.putExtra("bundle", bundle)
        startActivity(i)


    }

}
