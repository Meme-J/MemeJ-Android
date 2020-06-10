package com.example.memej.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.memej.MainActivity
import com.example.memej.R
import com.example.memej.Utils.Communicator
import com.example.memej.adapters.MemeWorldAdapter
import com.example.memej.adapters.OnItemClickListenerMemeWorld
import com.example.memej.responses.memeWorldResponses.Meme_World
import com.example.memej.ui.MemeWorld.CompletedMemeActivity
import com.example.memej.viewModels.LikedMemesViewModel

class LikedMemes : AppCompatActivity(), OnItemClickListenerMemeWorld, Communicator {


    private val viewModel: LikedMemesViewModel by viewModels()
    private lateinit var rv: RecyclerView
    private lateinit var memeWorldAdapter: MemeWorldAdapter
    lateinit var root: View
    lateinit var comm: Communicator
    lateinit var pb: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.liked_memes_fragment)

        pb = findViewById(R.id.pb_liked_memes)
        rv = findViewById(R.id.rv_likedMemes)

        //Reinstantiate the toolbar properties
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.tb_likedMemes)
        toolbar.setNavigationOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }

        //Instantiate ViewModel
        memeWorldAdapter = MemeWorldAdapter(this, this)
        initList()


        val swl = findViewById<SwipeRefreshLayout>(R.id.swl_liked_memes)
        swl.setOnRefreshListener {
            initList()
            swl.isRefreshing = false
        }


    }

    private fun initList() {

        viewModel.getPosts(pb = pb).observe(this, Observer<PagedList<Meme_World>> { pagedList ->
            memeWorldAdapter.submitList(pagedList)
        })


        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = memeWorldAdapter
        pb.visibility = View.GONE
    }


    override fun onItemClicked(_homeMeme: Meme_World) {
        //Replace with the same as Meme World
        //Meme World Container
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

        openLikedMemeFromActivity(bundle)

    }

    override fun passDataFromHome(bundle: Bundle) {

    }

    override fun passDataToMemeWorld(bundle: Bundle) {
    }

    override fun goToLikedMemesPage() {
    }

    override fun goToMemesByTagPage(bundle: Bundle) {
    }

    override fun goBackToHomePage() {
    }

    override fun goToSearchResult(bundle: Bundle) {
    }

    override fun goToProfilePage() {
    }

    override fun openLikedMemeFromActivity(bundle: Bundle) {
        //Replace with the new meme World Frgment
        val transaction = this.supportFragmentManager.beginTransaction()
        val frag2 = CompletedMemeActivity()     //This is Fragmnet
        frag2.arguments = bundle

        transaction.replace(R.id.container_likedMemes, frag2)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()

    }

}
