package com.example.memej.ui.MyDrafts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.memej.R

class MyDraftsFragment : Fragment() {

    companion object {
        fun newInstance() = MyDraftsFragment()
    }

    private lateinit var viewModel: MyDraftsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_drafts_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MyDraftsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
