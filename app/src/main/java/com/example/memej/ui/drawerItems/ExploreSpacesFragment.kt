package com.example.memej.ui.drawerItems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.memej.R
import com.example.memej.viewModels.ExploreSpacesViewModel

class ExploreSpacesFragment : Fragment() {

    companion object {
        fun newInstance() = ExploreSpacesFragment()
    }

    private lateinit var viewModel: ExploreSpacesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.explore_spaces_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ExploreSpacesViewModel::class.java)


    }

}
