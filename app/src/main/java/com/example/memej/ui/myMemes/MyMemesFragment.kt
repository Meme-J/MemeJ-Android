package com.example.memej.ui.myMemes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.memej.R
import com.example.memej.viewModels.MyMemesViewModel

class MyMemesFragment : Fragment() {

    companion object {
        fun newInstance() = MyMemesFragment()
    }

    private val viewModel: MyMemesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_memes_fragment, container, false)
    }

}
