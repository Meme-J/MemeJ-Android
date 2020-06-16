package com.example.memej.ui.memeTemplate

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.adapters.MemeGroupAdapter
import com.example.memej.adapters.OnItemClickListenerTemplate
import com.example.memej.responses.template.EmptyTemplateResponse
import com.example.memej.viewModels.SelectMemeGroupViewModel
import kotlinx.android.synthetic.main.activity_select_meme_template.*

class SelectMemeTemplateActivity : AppCompatActivity(), OnItemClickListenerTemplate {


    private lateinit var memeGroupAdapter: MemeGroupAdapter
    private lateinit var rv: RecyclerView
    private val viewModel: SelectMemeGroupViewModel by viewModels()
    lateinit var pb: ProgressBar
    lateinit var sessionManager: SessionManager
    lateinit var bundle: Bundle
    lateinit var toolbar: androidx.appcompat.widget.Toolbar


    override fun onItemClickedForTemplate(mg: EmptyTemplateResponse.Template) {

        //When we click a new template to be created

        pb_template.visibility = View.VISIBLE

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

        pb.visibility = View.GONE

        val i = Intent(this, NewMemeContainer::class.java)
        i.putExtra("bundle", bundle)
        startActivity(i)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_meme_template)

        //BAck NAvigation
        pb = findViewById(R.id.pb_template)
        pb.visibility = View.VISIBLE
        toolbar = findViewById(R.id.addMemeTb)
        //Will go back as any other activity

        pb = findViewById(R.id.pb_template)
        sessionManager =
            SessionManager(this)

        rv = findViewById(R.id.rv_memeGroup)
        memeGroupAdapter = MemeGroupAdapter(this)

        observeLiveData()
        initializeList()


    }

    private fun observeLiveData() {
        viewModel.getPosts(pb = pb).observe(this, Observer {
            memeGroupAdapter.submitList(it)
        })
    }

    private fun initializeList() {
        rv.layoutManager = GridLayoutManager(this, 2)
        rv.adapter = memeGroupAdapter
        //After this has been done, close the pb
        pb.visibility = View.GONE
    }


}
