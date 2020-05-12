package com.example.memej.ui.memeTemplate

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.adapters.MemeGroupAdapter
import com.example.memej.adapters.OnItemClickListener
import com.example.memej.entities.memeGroup
import kotlinx.android.synthetic.main.activity_select_meme_template.*

class SelectMemeTemplateActivity : AppCompatActivity(), OnItemClickListener {


    private lateinit var memeGroupAdapter: MemeGroupAdapter
    lateinit var viewmodel: SelectMemeGroupViewModel
    private lateinit var rv: RecyclerView

    override fun onItemClicked(mg: memeGroup) {
        Toast.makeText(this, "Tag ${mg.tag} ", Toast.LENGTH_LONG)
            .show()
        //Navigate to the next Item
        //Manage a request to get the desired tag
        //Pass tag intent to the next fragmnet
        val bundle = bundleOf(
            "tag_name" to mg.tag,
            "tag_id" to mg.memeGroupId
        )
        //ViewModel for onClick
        //To get what is required
//        val i = Intent(this, MemeTempatesFragment.javaClass)
//        startActivity(i,bundle)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_meme_template)

//        val tb: MaterialToolbar = findViewById(R.id.addMemeTb)
        setSupportActionBar(addMemeTb)
        //Back navigate. Method for better nav


        //Initialize ViewModel
        viewmodel = ViewModelProviders.of(this).get(SelectMemeGroupViewModel::class.java)
        //Initialize the recylcer View
        Log.e("K", "Vm Initialized")
        rv = findViewById(R.id.rv_memeGroup)

        //Initialize the adapter
        memeGroupAdapter = MemeGroupAdapter(this)
//        Log.e("K", "RV Initialized")
//        //Observe for changes
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

        viewmodel.getPosts().observe(this, Observer {
            memeGroupAdapter.submitList(it)
        })
    }

    private fun initializeList() {
        Log.e("K", "In IL")

        rv.layoutManager = GridLayoutManager(this, 2)
        rv.adapter = memeGroupAdapter
    }
}
