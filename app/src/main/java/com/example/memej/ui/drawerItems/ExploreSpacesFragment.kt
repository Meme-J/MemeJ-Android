package com.example.memej.ui.drawerItems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.memej.R
import com.example.memej.viewModels.ExploreSpacesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ExploreSpacesFragment : Fragment() {

    companion object {
        fun newInstance() = ExploreSpacesFragment()
    }

    private val viewModel: ExploreSpacesViewModel by viewModels()


    lateinit var root: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.explore_spaces_fragment, container, false)

        //Init the recycler view
        //Add notation for Observe List from the Workspace Tags

        val fab = root.findViewById<FloatingActionButton>(R.id.fab_workspace)
        fab.setOnClickListener {
            Toast.makeText(requireContext(), "OKAJus", Toast.LENGTH_SHORT).show()
        }
        return root
    }


}
