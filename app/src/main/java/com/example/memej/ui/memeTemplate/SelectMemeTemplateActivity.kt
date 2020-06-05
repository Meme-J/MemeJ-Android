package com.example.memej.ui.memeTemplate

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.Utils.SessionManager
import com.example.memej.adapters.MemeGroupAdapter
import com.example.memej.adapters.OnItemClickListenerTemplate
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.template.EmptyTemplateResponse
import com.example.memej.ui.home.EditMemeContainerFragment
import kotlinx.android.synthetic.main.activity_select_meme_template.*
import retrofit2.Call
import retrofit2.Response

class SelectMemeTemplateActivity : AppCompatActivity(), OnItemClickListenerTemplate {


    private lateinit var memeGroupAdapter: MemeGroupAdapter
    private lateinit var rv: RecyclerView
    lateinit var viewModel: SelectMemeGroupViewModel
    lateinit var pb: ProgressBar
    lateinit var sessionManager: SessionManager
    lateinit var bundle: Bundle


    override fun onItemClickedForTemplate(mg: EmptyTemplateResponse.Template) {
        //Then, we will give the template id and put in Edit Fragment Controller
        pb_template.visibility = View.VISIBLE
        //Make call with mg.templateId

        //Make call for opening the template
        val service = RetrofitClient.makeCallsForMemes(this)
        service.openTemplate(mg._id, accessToken = "Bearer ${sessionManager.fetchAcessToken()}")
            .enqueue(object : retrofit2.Callback<EmptyTemplateResponse.Template> {
                override fun onFailure(call: Call<EmptyTemplateResponse.Template>, t: Throwable) {
                    Toast.makeText(
                        this@SelectMemeTemplateActivity,
                        t.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    pb.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<EmptyTemplateResponse.Template>,
                    response: Response<EmptyTemplateResponse.Template>
                ) {
                    if (response.code() == 200) {
                        //Able to fetch data
                        //Transmit in the EditContainer
                        //Response body is of type template
                        //create type safe bundle
                        bundle = bundleOf(

                            "imageUrl" to response.body()?.imageUrl,
                            "id" to response.body()?._id,
                            "textColorCode" to response.body()?.textColorCode,
                            "textSize" to response.body()?.textSize,
                            "numPlaceholders" to response.body()?.numPlaceholders,
                            "coordinate" to response.body()?.coordinates,
                            "tags" to response.body()?.tags,
                            "name" to response.body()?.name

                        )
                        replaceFragment(response.body())
                        pb.visibility = View.GONE


                    } else {
                        pb.visibility = View.GONE

                        Toast.makeText(
                            this@SelectMemeTemplateActivity,
                            response.errorBody().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            })

    }


    private fun replaceFragment(body: EmptyTemplateResponse.Template?) {

        //This is an activity
        val frag2 = EditMemeContainerFragment()
        val transaction = this.supportFragmentManager.beginTransaction()
        frag2.arguments = bundle
        transaction.replace(R.id.container2, frag2)             //Current Fragment
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_meme_template)

        //BAck NAvigation


        viewModel = ViewModelProviders.of(this).get(SelectMemeGroupViewModel::class.java)
        pb = findViewById(R.id.pb_template)
        sessionManager = SessionManager(this)
        //Initialzie RV, adapter
        rv = findViewById(R.id.rv_memeGroup)
        memeGroupAdapter = MemeGroupAdapter(this)

        observeLiveData()
        initializeList()


    }

    private fun observeLiveData() {
        viewModel.getPosts().observe(this, Observer {
            memeGroupAdapter.submitList(it)
        })
    }

    private fun initializeList() {
        rv.layoutManager = GridLayoutManager(this, 2)
        rv.adapter = memeGroupAdapter
        //After this has been done, close the pb
        findViewById<ProgressBar>(R.id.pb_template).visibility = View.GONE
    }


}
