package com.example.memej.ui.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.MainActivity
import com.example.memej.R
import com.example.memej.Utils.Communicator
import com.example.memej.Utils.PreferenceUtil
import com.example.memej.adapters.OnWorkSpaceChangedListener
import com.example.memej.adapters.WorkSpaceDialogAdapter
import com.example.memej.databinding.FragmentWorkspaceDialogBinding
import com.example.memej.responses.workspaces.UserWorkspaces
import com.example.memej.ui.workspace.CreateWorkspaceActivity
import com.example.memej.viewModels.WorkspaceDialogViewModel
import kotlinx.android.synthetic.main.fragment_workspace_dialog.view.*

class WorkspaceDialogFragment : DialogFragment(), OnWorkSpaceChangedListener {


    lateinit var builder: AlertDialog
    lateinit var dialogBinding: FragmentWorkspaceDialogBinding
    private lateinit var onDismissListener: DialogInterface.OnDismissListener
    private val viewModel: WorkspaceDialogViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterSpace: WorkSpaceDialogAdapter
    private lateinit var comm: Communicator
    private val preferencesUtils = PreferenceUtil

    private val currentSpace = preferencesUtils.getCurrentSpaceFromPreference()


    private val TAG = WorkspaceDialogFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fragment_workspace_dialog, container, false
        )

        val style = DialogFragment.STYLE_NO_FRAME
        val theme = R.style.MaterialAlertDialog_MaterialComponents_Title_Text
        setStyle(style, theme)

        return dialogBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerViewItems(view)
        setupClickListeners(view)
    }

    private fun setUpRecyclerViewItems(view: View) {
        recyclerView = view.rv_dialog_workspaces
        adapterSpace = WorkSpaceDialogAdapter(this)
        comm = activity as Communicator

        //Check the default space now

        //Get the current space from the preference Util
        loadTheSpaces()


    }


    private fun setupClickListeners(view: View) {

        view.extended_fab_main_dialog.setOnClickListener {
            goToCreateSpace()
        }


    }

    private fun goToCreateSpace() {
        val i = Intent(context, CreateWorkspaceActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context?.startActivity(i)
    }


    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener?) {
        this.onDismissListener = onDismissListener!!
    }


    override fun switchWorkspace(_workspace: UserWorkspaces.Workspace) {

        //1. Dismiss this fragment
        //2. Refresh start and recreate activity

        //Set the current workspace as _workspace

        preferencesUtils.setCurrentSpaceFromPreference(_workspace)

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)


    }


    //Load
    private fun loadTheSpaces() {


        viewModel.spacesFunction().observe(this, androidx.lifecycle.Observer { mResponse ->
            val success = viewModel.successfulSpaces.value
            if (success != null) {
                if (success) {
                    initAdapterForSpaceDialog(mResponse)
                } else {
                    //The response is null
                    //Log the message
                    Log.e(TAG, viewModel.messageSpace.value.toString())
                    //Show something in the dialog to that says could not load workspaces at the moment
                    //TODO: Remove network dependency here
                    //Use repositories to store this
                }
            }
        })

        //Don't remove these observers


    }

    private fun initAdapterForSpaceDialog(response: UserWorkspaces) {

        adapterSpace.workSpacesList = response.workspaces
        recyclerView.adapter = adapterSpace
        adapterSpace.notifyDataSetChanged()


    }


}


