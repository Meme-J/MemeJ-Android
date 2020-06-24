package com.example.memej.ui.memeTemplate

import android.app.ProgressDialog
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
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.adapters.MemeGroupAdapter
import com.example.memej.adapters.OnItemClickListenerTemplate
import com.example.memej.responses.template.EmptyTemplateResponse
import com.example.memej.viewModels.SelectMemeGroupViewModel
import com.shreyaspatil.MaterialDialog.MaterialDialog

class SelectMemeTemplateActivity : AppCompatActivity(), OnItemClickListenerTemplate {


    private lateinit var memeGroupAdapter: MemeGroupAdapter
    private lateinit var rv: RecyclerView
    private val viewModel: SelectMemeGroupViewModel by viewModels()
    lateinit var pb: ProgressBar
    lateinit var sessionManager: SessionManager
    lateinit var bundle: Bundle
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var dialog: ProgressDialog


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

        if (ErrorStatesResponse.checkIsNetworkConnected(this)) {
            observeLiveData()

        } else {
            checkConnection()
        }
        dialog.dismiss()

        pb.isIndeterminate = false
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
