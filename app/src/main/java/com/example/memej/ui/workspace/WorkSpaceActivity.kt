package com.example.memej.ui.workspace

import android.R.attr.label
import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.adapters.TagEditAdapter
import com.example.memej.body.GenerateLinkBody
import com.example.memej.body.SendWorkspaceRequestBody
import com.example.memej.databinding.ActivityWorkSpaceBinding
import com.example.memej.viewModels.WorkspaceViewModel
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_work_space.*


class WorkSpaceActivity : AppCompatActivity() {

    lateinit var binding: ActivityWorkSpaceBinding
    lateinit var arg: Bundle
    lateinit var sessionManager: SessionManager
    private val viewModel: WorkspaceViewModel by viewModels()

    private lateinit var SPACE_NAME: String
    private lateinit var SPACE_ID: String


    //Init the users for the create users
    lateinit var rv: RecyclerView
    lateinit var adapterUsersAdded: TagEditAdapter
    lateinit var mutableList: MutableList<String>
    lateinit var stringAdapter: ArrayAdapter<String>

    lateinit var d: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_work_space)
        arg = intent?.getBundleExtra("bundle")!!

        sessionManager = SessionManager(this)
        //We will have intents and the corresponding data

        //This condition will be used when we have been sending the id and name of the workspace to load the contents again
        //binding.swlWorkspace.isRefreshing = true

        //SPACE_ID = arg.getString("id")
        val toolbar = binding.tbWorkspace
        //Get the name, likes, people, and number of memes of the space
        val space_name = arg.getString("name")
        toolbar.title = space_name
        SPACE_NAME = space_name.toString()
        SPACE_ID = arg.getString("id")!!
        binding.tvWorkspaceName.text = space_name
//        val ppl = arg.getInt()
//        val memes = arg.getInt()
//        val loves = arg.getInt()
//        binding.apply {
//            tvPeople.text = ppl.toString()
//            tvMemes.text = memes.toString()
//            tvLikes.text = loves.toString()
//        }


        //Navigate back
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        //In case the user presses swl
        binding.swlWorkspace.setOnRefreshListener {
            binding.swlWorkspace.isRefreshing = false
        }


        //Exit a workspace
        binding.btnExit.setOnClickListener {
            exitSpace()
        }


        binding.btnInvite.setOnClickListener {
            inviteByUsers()
        }

        binding.btnLink.setOnClickListener {
            getLink()
        }

    }

    private fun getLink() {
        //Create progress dialog
        val pb = ProgressDialog(this)
        pb.setMessage("Generating link")
        pb.show()


        val w = GenerateLinkBody.Workspace(SPACE_ID, SPACE_NAME)
        val body = GenerateLinkBody(w)
        val response = viewModel.generateLink(body)?.value

        pb.dismiss()
        val success = viewModel.generateLinkBool.value
        val message = viewModel.messageLink.value

        if (response == null) {
            Snackbar.make(container_workspace, message.toString(), Snackbar.LENGTH_LONG).show()
        } else {
            val link = response.link
            shareLinkDialog(link)
        }


    }

    private fun shareLinkDialog(link: String) {
        copyToClipboard(link)
        createIntent(link)

    }

    private fun createIntent(link: String) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        // Add data to the intent, the receiving app will decide
        // what to do with it.

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_TEXT, link)
        startActivity(Intent.createChooser(share, "Share link: "))

    }

    private fun copyToClipboard(link: String) {
        val clipboard: ClipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label.toString(), link)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    private fun inviteByUsers() {
        createEnterUserDialog()
    }


    private fun exitSpace() {
        createExitDialog()
    }

    private fun createExitDialog() {
        android.app.AlertDialog.Builder(this)
            .setTitle("Exit space")
            .setMessage("Are you sure you want to exit this space?")
            .setPositiveButton("Yes") { d, _ ->
                d.dismiss()
                confirmExit()
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .show()
    }

    private fun createEnterUserDialog() {

        d = AlertDialog.Builder(this)
        val v = layoutInflater.inflate(R.layout.layout_invite_users, container_workspace)
        d.setView(v)


        rv = v.findViewById<RecyclerView>(R.id.rv_insertedUsersForInvite)
        adapterUsersAdded = TagEditAdapter()
        mutableList = mutableListOf<String>()           //Empty list
        stringAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line)
        val pb = v.findViewById<ProgressBar>(R.id.pb_load_users)
        val et = v.findViewById<AutoCompleteTextView>(R.id.the_id)


        //OnClickVal
        val onItemClickTag =
            AdapterView.OnItemClickListener { adapterView, view, i, l ->
                mutableList.add(adapterView.getItemAtPosition(i).toString())
                setInUserRv(rv)
                et.text = null
            }

        //Positive and negative buttons

        //Create observer on Text
        et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //Use the string as the name to search
                search(s, et)
                pb.isVisible = false
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //The small progrss bar at the end
                pb.isVisible = true
            }
        })

        et.onItemClickListener = onItemClickTag

        //Get the button
        val btn = v.findViewById<MaterialButton>(R.id.mtb_send_invites)
        btn.isEnabled = mutableList.size > 0

        btn.setOnClickListener {
            sendInvites()
        }

        d.show()


    }

    private fun sendInvites() {

        //Use the viewmodel
        val body = SendWorkspaceRequestBody(mutableList, SPACE_ID, SPACE_NAME)
        val response = viewModel.inviteUsers(body)
        val success = viewModel.sendBool.value
        val message = viewModel.messageSend.value

        if (response == null) {

            d.setOnDismissListener { }

            createSnackbar(message)
        } else {

            createSnackbar(message)
            //Successfully sent the requests
            //Close dialog
            d.setOnDismissListener {

            }


        }


    }

    private fun setInUserRv(rv: RecyclerView) {
        //Reference to rv
        val layoutManager: FlexboxLayoutManager = FlexboxLayoutManager(this)
        layoutManager.alignItems = AlignItems.BASELINE
        layoutManager.justifyContent = JustifyContent.CENTER
        layoutManager.flexWrap = FlexWrap.WRAP

        rv.layoutManager = layoutManager
        adapterUsersAdded.tagAdded = mutableList
        rv.adapter = adapterUsersAdded

    }

    private fun search(s: Editable?, et: AutoCompleteTextView) {
        //Use ViewModel Observables
        val toBeSearchedString = s.toString()

        val response = viewModel.searchUsers(toBeSearchedString)
        val success = viewModel.inviteBool.value
        val message = viewModel.messageInvite.value

        //If the success if false
        if (response == null) {
            //Use the message and the false success
            createSnackbar(message)
        } else {

            //When response is not null and a list of suggestions
            val suggestions = response.suggestions
            val str = mutableListOf<String>()
            for (y in suggestions) {
                str.add(y.username)
            }

            //Reinit the str adapter
            stringAdapter =
                ArrayAdapter(
                    this,
                    android.R.layout.simple_dropdown_item_1line,
                    str
                )

            et.setAdapter(stringAdapter)

        }

    }

    private fun createSnackbar(message: String?) {
        Snackbar.make(container_workspace, message.toString(), Snackbar.LENGTH_SHORT).show()
    }


    private fun confirmExit() {

//        val id = arg.getString()
//        val name = arg.getString()
//        val body = ExitWorkspaceBody.Workspace(id, name)
//        val resp = viewModel.exitSpace(body)
//
//        if (resp == null) {
//            //Unable to exit
//            val snack = Snackbar.make(
//                container_workspace,
//                "Unable to exit the workspace",
//                Snackbar.LENGTH_SHORT
//            )
//            snack.show()
//        } else {
//            Toast.makeText(this, "Exited the space", Toast.LENGTH_SHORT).show()
//
//            //TODO:Update the util data of the space
//            //TODO:Use room
//
//            //TODO:Set default to global space
//            val i = Intent(this, MySpacesFragmnet::class.java)
//            startActivity(i)
//            finish()
//            finishAffinity()
//        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}
