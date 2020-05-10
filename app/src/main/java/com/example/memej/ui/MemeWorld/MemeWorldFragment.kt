package com.example.memej.ui.MemeWorld

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.memej.R

class MemeWorldFragment : Fragment() {

    companion object {
        fun newInstance() = MemeWorldFragment()
    }

    private lateinit var viewModel: MemeWorldViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.meme_world_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MemeWorldViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
