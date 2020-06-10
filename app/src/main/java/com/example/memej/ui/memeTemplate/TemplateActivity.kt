package com.example.memej.ui.memeTemplate

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.adapters.MemeTemplateAdapter
import com.example.memej.adapters.OnItemClickListenerMeme
import com.example.memej.databinding.ActivityTemplateBinding
import com.example.memej.entities.memeTemplate
import com.example.memej.viewModels.TemplateViewModel
import com.google.android.material.textview.MaterialTextView

class TemplateActivity : AppCompatActivity(), OnItemClickListenerMeme {

    private lateinit var memeTemplate: MemeTemplateAdapter
    private val viewmodel: TemplateViewModel by viewModels()
    private lateinit var rv: RecyclerView
    var meme_group_id: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val b: ActivityTemplateBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_template)
        //get data from intenet

        val bundle: Bundle? = intent.extras
        val tag: MaterialTextView = findViewById(R.id.memeTag)
        val t: String? = bundle?.getString("tag")
        tag.text = t
        //Get id and use it
        val id: Int? = bundle?.getInt("id")
        meme_group_id = id
        //Initialze VM, rv, adapter
        rv = b.rvMemeTemplate
        memeTemplate = MemeTemplateAdapter(this)

        observeLiveData()
        Log.e("K", "Returns from observe Live data")

        //Get data
        initializeList()
        Log.e("K", "Returns from Initialized list")

        //Ovveride methods for onClick

    }

    private fun observeLiveData() {
        //observe live data emitted by view model
        Log.e("K", "In OLD")

        meme_group_id?.let { it ->
            viewmodel.getPosts(it).observe(this, Observer {
                memeTemplate.submitList(it)
            })
        }
    }

    private fun initializeList() {
        Log.e("K", "In IL")

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = memeTemplate
    }

    override fun onItemClickedForTemplateForMeme(_memeTemplate: memeTemplate) {
        //Implementation


    }

}