package com.example.memej.ui.home

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.Utils.Communicator
import com.example.memej.adapters.OnWorkSpaceChangedListener
import com.example.memej.adapters.WorkSpaceDialogAdapter
import com.example.memej.databinding.FragmentWorkspaceDialogBinding
import com.example.memej.responses.workspaces.UserWorkspaces
import com.example.memej.ui.drawerItems.ExploreSpacesFragment
import com.example.memej.viewModels.WorkspaceDialogViewModel

class WorkspaceDialogFragment : Fragment(), OnWorkSpaceChangedListener {


    lateinit var builder: AlertDialog
    lateinit var dialogBinding: FragmentWorkspaceDialogBinding
    private lateinit var onDismissListener: DialogInterface.OnDismissListener
    private val viewModel: WorkspaceDialogViewModel by viewModels()
    lateinit var rv: RecyclerView
    lateinit var adapter: WorkSpaceDialogAdapter
    lateinit var comm: Communicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fragment_workspace_dialog, null, false
        )

        rv = dialogBinding.rvDialogWorkspaces
        adapter = WorkSpaceDialogAdapter(this)
        comm = activity as Communicator

        //Add a workspace
        dialogBinding.llAddWorkspace.setOnClickListener {
            goToWorkSpaceChannel()
        }


        return dialogBinding.root
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        dialogBinding = DataBindingUtil.inflate(
//            LayoutInflater.from(context),
//            R.layout.fragment_workspace_dialog, null, false
//        )
//
//        val dialogBuilder = AlertDialog.Builder(requireContext())
//        dialogBuilder.setView(dialogBinding.root)
//        dialogBuilder.setTitle(R.string.workspaces)
//
//        builder = dialogBuilder.create()
//
//        return builder
//    }

    private fun goToWorkSpaceChannel() {
        comm.goToAFragmnet(ExploreSpacesFragment())

    }

    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener?) {
        this.onDismissListener = onDismissListener!!
    }


    override fun switchWorkspace(_workspace: UserWorkspaces.Workspace) {

    }


}


