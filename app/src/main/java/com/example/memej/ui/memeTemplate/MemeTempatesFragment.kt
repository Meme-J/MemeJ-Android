package com.example.memej.ui.memeTemplate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.memej.R
import com.google.android.material.textview.MaterialTextView

class MemeTempatesFragment : Fragment() {

    companion object {
        fun newInstance() = MemeTempatesFragment()
    }

    private lateinit var viewModel: MemeTempatesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.meme_tempates_fragment, container, false)

        //Test
        val t = root.findViewById<MaterialTextView>(R.id.memeTag)
        // t.text = this.arguments?.getString("tag_name")


        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MemeTempatesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
