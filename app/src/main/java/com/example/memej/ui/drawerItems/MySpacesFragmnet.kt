package com.example.memej.ui.drawerItems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.memej.R
import com.example.memej.viewModels.MySpacesFragmnetViewModel

class MySpacesFragmnet : Fragment() {

    companion object {
        fun newInstance() = MySpacesFragmnet()
    }

    private lateinit var viewModel: MySpacesFragmnetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_spaces_fragmnet_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MySpacesFragmnetViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
