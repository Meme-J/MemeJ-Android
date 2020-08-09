package com.example.memej.ui.drawerItems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.memej.R
import com.example.memej.databinding.MySpacesFragmnetFragmentBinding
import com.example.memej.viewModels.MySpacesFragmnetViewModel

class MySpacesFragmnet : Fragment() {

    companion object {
        fun newInstance() = MySpacesFragmnet()
    }

    private val viewModel: MySpacesFragmnetViewModel by viewModels()
    lateinit var b: MySpacesFragmnetFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = DataBindingUtil.inflate(
            inflater,
            R.layout.my_spaces_fragmnet_fragment,
            container,
            false
        )












        return b.root
    }


}
