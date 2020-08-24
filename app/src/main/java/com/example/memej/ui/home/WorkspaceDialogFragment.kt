package com.example.memej.ui.home

//class WorkspaceDialogFragment : DialogFragment(), OnWorkSpaceChangedListener {
//
//
//    lateinit var builder: AlertDialog
//    lateinit var dialogBinding: FragmentWorkspaceDialogBinding
//    private lateinit var onDismissListener: DialogInterface.OnDismissListener
//    private val viewModel: WorkspaceDialogViewModel by viewModels()
//    lateinit var rv: RecyclerView
//    lateinit var adapter: WorkSpaceDialogAdapter
//    lateinit var comm: Communicator
//    private val preferencesUtils = PreferenceUtil
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        dialogBinding = DataBindingUtil.inflate(
//            LayoutInflater.from(context),
//            R.layout.fragment_workspace_dialog, null, false
//        )
//        val style = DialogFragment.STYLE_NO_FRAME
//        val theme = R.style.MaterialAlertDialog_MaterialComponents_Title_Text
//        setStyle(style, theme)
//
//        return dialogBinding.root
//    }
//
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        setUpRecyclerViewItems(view)
//        setupClickListeners(view)
//    }
//
//    private fun setUpRecyclerViewItems(view: View) {
//        rv = view.rv_dialog_workspaces
//        adapter = WorkSpaceDialogAdapter(this)
//        comm = activity as Communicator
//        //Check the default space now
//        if (preferencesUtils.current_space == "Global Space") {
//            //Apply shader on Global space
//            view.cslt_global.setBackgroundColor(resources.getColor(R.color.spaceShader))
//        }
//
//
//    }
//
//    override fun onStart() {
//        super.onStart()
//
//
//        val p = RelativeLayout.LayoutParams(
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.MATCH_PARENT
//        )
//        p.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
//
//
//        val d = dialog?.window
//        val decv = d?.decorView
//        decv?.layoutParams = p
//
//
//    }
//
//    private fun setupClickListeners(view: View) {
//        view.ll_addWorkspace.setOnClickListener {
//            goToCreateSpace()
//        }
//
//
//    }
//
//    private fun goToCreateSpace() {
//        val i = Intent(context, CreateWorkspaceActivity::class.java)
//        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        context?.startActivity(i)
//    }
//
//
//    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener?) {
//        this.onDismissListener = onDismissListener!!
//    }
//
//
//    override fun switchWorkspace(_workspace: UserWorkspaces.Workspace) {
//
//    }
//
//
//}
//
//
