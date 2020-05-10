package com.example.memej.ui.myMemes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.memej.R

class MyMemesFragment : Fragment() {

    companion object {
        fun newInstance() = MyMemesFragment()
    }

    private lateinit var viewModel: MyMemesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_memes_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MyMemesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
