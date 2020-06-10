package com.example.memej.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.MainActivity
import com.example.memej.R
import com.example.memej.Utils.Communicator2
import com.example.memej.adapters.HomeMemeAdapter
import com.example.memej.adapters.MemeWorldAdapter
import com.example.memej.adapters.OnItemClickListenerHome
import com.example.memej.adapters.OnItemClickListenerMemeWorld
import com.example.memej.responses.homeMememResponses.Meme_Home
import com.example.memej.responses.memeWorldResponses.Meme_World
import com.example.memej.ui.MemeWorld.CompletedMemeActivity
import com.example.memej.viewModels.SearchResultActivityViewModel


class SearchResultActivity : AppCompatActivity(), OnItemClickListenerHome,
    OnItemClickListenerMemeWorld, Communicator2 {

    var tagName: String? = ""              //Deafult Query
    var type: String? = "ongoing"           //Default
    lateinit var arg: Bundle
    lateinit var rv: RecyclerView
    lateinit var adapterOnGoing: HomeMemeAdapter
    lateinit var adapterComplete: MemeWorldAdapter
    lateinit var pb: ProgressBar
    lateinit var viewmodel: SearchResultActivityViewModel
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)


        arg = intent?.getBundleExtra("bundle")!!
        //The toolbar will be automatically set
        toolbar = findViewById(R.id.tb_searchResult)

        tagName = arg.getString("tag")
        type = arg.getString("type")
        toolbar.title = tagName

        //Navigate back
        toolbar.setNavigationOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
        //Init Viewmmodel
        viewmodel = ViewModelProviders.of(this).get(SearchResultActivityViewModel::class.java)
        pb = findViewById(R.id.pb_template)
        pb.visibility = View.VISIBLE
        //Init RV

        rv = findViewById(R.id.rv_memes_search)
        if (type == "complete") {

            getCompleteMemes()
        } else {

            getOngoingMemes()
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

    }


    private fun getCompleteMemes() {

        adapterComplete = MemeWorldAdapter(this, this)
        viewmodel.getComplete(tagName.toString(), pb).observe(this, Observer {
            adapterComplete.submitList(it)
        })


        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapterComplete
        pb.visibility = View.GONE
    }


    override fun onItemClicked(_homeMeme: Meme_World) {
        //We are travelling in the same main actvity
        //Getting the image and everything
        val bundle = bundleOf(
            "id" to _homeMeme._id,
            "lastUpdated" to _homeMeme.lastUpdated,
            "likedBy" to _homeMeme.likedBy,
            "likes" to _homeMeme.likes,
            "placeholders" to _homeMeme.placeholders,
            "meme" to _homeMeme.templateId.numPlaceholders,
            "tags" to _homeMeme.tags,
            "users" to _homeMeme.users,
            "templateIdCoordinates" to _homeMeme.templateId.coordinates,
            "imageUrl" to _homeMeme.templateId.imageUrl,
            "imageTags" to _homeMeme.templateId.tags,
            "imageName" to _homeMeme.templateId.name,
            "textSize" to _homeMeme.templateId.textSize,
            "textColor" to _homeMeme.templateId.textColorCode
        )

        passDtataToComplete(bundle)

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
        passDataToEdit(bundle)

    }

    override fun passDataFromADdToNewMeme(bundle: Bundle) {

    }


    override fun returnToSelectTemplate() {


    }

    override fun passDataToEdit(bundle: Bundle) {

        val transaction = this.supportFragmentManager.beginTransaction()
        val frag2 = EditMemeContainerFragment()

        frag2.arguments = bundle
        transaction.replace(R.id.container, frag2)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()


    }

    override fun passDtataToComplete(bundle: Bundle) {

        val transaction = this.supportFragmentManager.beginTransaction()
        val frag2 = CompletedMemeActivity()

        frag2.arguments = bundle
        transaction.replace(R.id.container, frag2)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()


    }
}