package com.example.memej.ui.workspace

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.memej.R
import com.example.memej.databinding.ActivityCreateWorkspaceBinding
import com.example.memej.viewModels.CreateWorkspaceViewmodel
import com.google.android.material.snackbar.Snackbar
import com.shreyaspatil.MaterialDialog.MaterialDialog
import kotlinx.android.synthetic.main.activity_blank_workspace.*
import kotlinx.android.synthetic.main.activity_create_workspace.*

class CreateWorkspaceActivity : Fragment() {

    lateinit var b: ActivityCreateWorkspaceBinding
    lateinit var mutableList: MutableList<String>
    private val viewModel: CreateWorkspaceViewmodel by viewModels()
    private var nameExists: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = DataBindingUtil.inflate(
            inflater,
            R.layout.activity_create_workspace,
            container_blank_space,
            true
        )

        mutableList = mutableListOf()
        val til = b.tilSpaceName
        til.setEndIconActivated(false)

        b.etSpaceName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
//                if (s!!.length > 1) {
//                    checkExistenceOfWorkSpace(s)
//                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })


//        til.addOnEditTextAttachedListener {
//
//        }
//
//        til.addOnEndIconChangedListener { textInputLayout, previousIcon ->
//
//        }

//        b.autoCompleteTagWorkspace.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                //Enable and disable
//                b.tagEditWorkspaceCreate.isEnabled = s?.length != 0
//
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//            }
//        })
//
//        b.tagEditWorkspaceCreate.setOnClickListener {
//            mutableList.add(b.autoCompleteTagWorkspace.text.toString())
//            addTagToRv()
//            b.autoCompleteTagWorkspace.text = null
//        }


        b.btnCreateSpace.setOnClickListener {
            createSpace()
        }


        return b.root
    }

    private fun createSpace() {
        //Validate name
        if (validateName()) {
            createConfirmDialog()
        }
    }

    private fun createConfirmDialog() {

        val name = b.etSpaceName.text.toString()
        val mDialog = MaterialDialog.Builder(context as Activity)
            .setTitle("Create workspace")
            .setMessage("Create a workspace named $name?")
            .setCancelable(true)
            .setPositiveButton(
                "CREATE",
                R.drawable.ic_add
            ) { dialogInterface, which ->
                dialogInterface.dismiss()
                postSpace()

            }
            .setNegativeButton(
                "REVIEW"
            ) { dialogInterface, which ->
                dialogInterface.dismiss()
            }
            .build()
        mDialog.show()


    }

    private fun postSpace() {

        //Create a profress dialog
        val dialog = ProgressDialog(context as Activity)
        dialog.setMessage("Creating workspace")
        dialog.show()

        val space_name = b.etSpaceName.text.toString()
        val space_initial_tags = mutableList

        val postMessage = viewModel.createSpace(space_name, space_initial_tags)

        if (postMessage == "Workspace created successfully.") {
            val snack =
                Snackbar.make(conatiner_create_workspace, postMessage, Snackbar.LENGTH_SHORT)
            snack.show()
            dialog.dismiss()
            //Go to my spaces activty


        } else {
            dialog.dismiss()
            val snack =
                Snackbar.make(conatiner_create_workspace, postMessage, Snackbar.LENGTH_SHORT)
            snack.show()
        }

    }

    private fun validateName(): Boolean {
        var isValid = false

        if (b.etSpaceName.text!!.isEmpty()) {
            isValid = false
            b.tilSpaceName.error = getString(R.string.workspace_empty)

        }
        return isValid

    }

//    private fun checkExistenceOfWorkSpace(s: Editable?) {
//
//        nameExists = viewModel.checkSpace(s!!)
//
//        updateEndIcon()
//
//
//    }

//    private fun updateEndIcon() {
//        if (nameExists) {
//            b.tilSpaceName.endIconDrawable!!.setTint(resources.getColor(R.color.red_heart))
//            b.tilSpaceName.error = getString(R.string.workspace_already_exits)
//        } else {
//            b.tilSpaceName.endIconDrawable!!.setTint(resources.getColor(R.color.green_700))
//            b.tilSpaceName.error = null
//        }
//    }

//    private fun addTagToRv() {
//        val rvTagEdits = b.rvInsertedTagsSpaceCreate
//        val HorizontalLayoutInsertedTags: LinearLayoutManager = LinearLayoutManager(
//            this,
//            LinearLayoutManager.HORIZONTAL,
//            false
//        )
//        val adapterTagAdded = TagEditAdapter()
//        adapterTagAdded.tagAdded = mutableList
//        rvTagEdits.layoutManager = HorizontalLayoutInsertedTags
//        rvTagEdits.adapter = adapterTagAdded
//    }


}
