package com.example.memej.ui.drawerItems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.memej.R
import com.example.memej.viewModels.InvitesFragmnetViewModel

class InvitesFragmnet : Fragment() {

    companion object {
        fun newInstance() = InvitesFragmnet()
    }

    private lateinit var viewModel: InvitesFragmnetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.invites_fragmnet_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(InvitesFragmnetViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
