package com.example.memej.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.PreferenceUtil
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.adapters.LikedMemesAdapter
import com.example.memej.adapters.OnItemClickListenerLikeMeme
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.NumLikes
import com.example.memej.responses.ProfileResponse
import com.example.memej.responses.memeWorldResponses.Meme_World
import com.example.memej.ui.MemeWorld.CompletedMemeActivity
import com.example.memej.viewModels.ProfileViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.textview.MaterialTextView
import com.shreyaspatil.MaterialDialog.MaterialDialog
import retrofit2.Call
import retrofit2.Response
import java.util.*


class
ProfileFragment : Fragment(), OnItemClickListenerLikeMeme {

    companion object {
        fun newInstance() = ProfileFragment()
        const val SWITCH_BOUND = 0.8f
        const val TO_EXPANDED = 0
        const val TO_COLLAPSED = 1
        const val WAIT_FOR_SWITCH = 0
        const val SWITCHED = 1

    }

    private lateinit var root: View
    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var ivUserAvatar: CardView
    private lateinit var textAvatar: TextView
    private var EXPAND_AVATAR_SIZE: Float = 0F
    private var COLLAPSE_IMAGE_SIZE: Float = 0F
    private var horizontalToolbarAvatarMargin: Float = 0F
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var appBarLayout: AppBarLayout
    private var cashCollapseState: Pair<Int, Int>? = null
    private lateinit var titleToolbarText: MaterialTextView
    private lateinit var titleToolbarTextSingle: MaterialTextView
    private lateinit var invisibleTextViewWorkAround: MaterialTextView
    private lateinit var background: FrameLayout

    /**/
    private var avatarAnimateStartPointY: Float = 0F
    private var avatarCollapseAnimationChangeWeight: Float = 0F
    private var isCalculated = false
    private var verticalToolbarAvatarMargin = 0F


    private var username: String = ""
    private var likesNum: String = ""
    private val preferenceUtils = PreferenceUtil
    private lateinit var sessionManager: SessionManager
    lateinit var rv: RecyclerView
    lateinit var adapter: LikedMemesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        root = inflater.inflate(R.layout.profile_fragment, container, false)

        sessionManager = SessionManager(requireContext())

        //Get users and its respected attributes
//        viewModel.successfulUpdateProfile.observe(viewLifecycleOwner, Observer { successful ->
//
//            if (successful != null) {
//                if (successful) {
//                    username = viewModel.profileUser.username
//                    setPreferencesUser(viewModel.profileUser)
//                } else {
//                    Log.e("Fail Profile", viewModel.messageProfile)
//                }
//            }
//        })
//
//        viewModel.successfulUpdateLikes.observe(viewLifecycleOwner, Observer { successful ->
//
//            if (successful != null) {
//                if (successful) {
//                    likesNum = viewModel.numberLikes.likes.toString()
//                    setPreferencesLikes(viewModel.numberLikes)
//                } else {
//                    Log.e("Fail Profile", viewModel.messageLikes)
//                }
//            }
//        })

        getUser()
        getLikes()
        callLikes()


        root.findViewById<TextView>(R.id.textViewNumberTotalLikes).text = likesNum
        //Get the avatar image

        //get the liked memes
        rv = root.findViewById<RecyclerView>(R.id.rv_likedMemesProfile)
        adapter = LikedMemesAdapter(requireContext(), this)

        getLikedMemes()

        EXPAND_AVATAR_SIZE = resources.getDimension(R.dimen.default_expanded_image_size)
        COLLAPSE_IMAGE_SIZE = resources.getDimension(R.dimen.default_collapsed_image_size)

        horizontalToolbarAvatarMargin = resources.getDimension(R.dimen.space)
        /* collapsingAvatarContainer = findViewById(R.id.stuff_container)*/
        appBarLayout = root.findViewById(R.id.app_bar_layout)
        toolbar = root.findViewById(R.id.anim_toolbar)
        ivUserAvatar = root.findViewById(R.id.imgb_avatar_wrap)
        titleToolbarText = root.findViewById(R.id.tv_profile_name)
        titleToolbarTextSingle = root.findViewById(R.id.tv_profile_name_single)
        background = root.findViewById(R.id.fl_background)
        invisibleTextViewWorkAround = root.findViewById(R.id.tv_workaround)
        textAvatar = root.findViewById(R.id.avatar_text)

        (toolbar.height - COLLAPSE_IMAGE_SIZE) * 2
        /**/
        appBarLayout.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, i ->
                if (isCalculated.not()) {
                    avatarAnimateStartPointY =
                        Math.abs((appBarLayout.height - (EXPAND_AVATAR_SIZE + horizontalToolbarAvatarMargin)) / appBarLayout.totalScrollRange)
                    avatarCollapseAnimationChangeWeight = 1 / (1 - avatarAnimateStartPointY)
                    verticalToolbarAvatarMargin = (toolbar.height - COLLAPSE_IMAGE_SIZE) * 2
                    isCalculated = true
                }
                /**/
                updateViews(Math.abs(i / appBarLayout.totalScrollRange.toFloat()))
            })

        //In the end, the name of all the tvs is Username
        titleToolbarText.text = username
        titleToolbarTextSingle.text = username
        invisibleTextViewWorkAround.text = username

        val rnd = Random()
        val currentColor =
            Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        ivUserAvatar.setCardBackgroundColor(currentColor)
        textAvatar.text = username.take(2).toUpperCase(Locale.ROOT)




        return root
    }

    private fun getLikedMemes() {

        viewModel.getPostsOfLikedMemes()
            .observe(viewLifecycleOwner, Observer<PagedList<Meme_World>> { pagedList ->
                adapter.submitList(pagedList)
            })


        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

    }

    private fun callLikes() {

        val service = RetrofitClient.makeCallForProfileParameters(requireContext())
        service.getNumLikesRecieved(accessToken = "Bearer ${sessionManager.fetchAcessToken()}")
            .enqueue(object : retrofit2.Callback<NumLikes> {
                override fun onFailure(call: Call<NumLikes>, t: Throwable) {


                }

                override fun onResponse(call: Call<NumLikes>, response: Response<NumLikes>) {
                    //Response is number of likes
                    if (response.isSuccessful) {

                        likesNum = response.body()?.likes.toString()
                        setPreferencesLikes(response.body()!!)
                    }
                }
            })


    }

    private fun setPreferencesLikes(likes: NumLikes) {
        preferenceUtils.setNumberOfLikesFromPreference(likes)
    }


    private fun getLikes() {

        if (preferenceUtils.likes == 0) {

            //Check connection here
            if (ErrorStatesResponse.checkIsNetworkConnected(requireContext())) {
                callLikes()
            }

        } else {

            likesNum = preferenceUtils.getNumberOfLikes().likes.toString()

        }

    }

    private fun getUser() {
        //If there is blank in the pref

        if (preferenceUtils.username == "") {

            //Check connection here
            if (ErrorStatesResponse.checkIsNetworkConnected(requireContext())) {
                callUser()
            } else {
                checkConnection()
            }
        } else {

            username = preferenceUtils.getUserFromPrefernece().username

        }
    }

    private fun callUser() {

        if (!ErrorStatesResponse.checkIsNetworkConnected(requireContext())) {
            checkConnection()
        }

        // viewModel.getProfileOfUser()

        val service = RetrofitClient.getAuthInstance()

        service.getUser(accessToken = "Bearer ${sessionManager.fetchAcessToken()}")
            .enqueue(object : retrofit2.Callback<ProfileResponse> {
                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {

                    if (response.isSuccessful) {

                        username = response.body()?.profile?.username!!
                        setPreferencesUser(response.body()?.profile!!)
                    }
                }
            })

    }

    private fun setPreferencesUser(user: ProfileResponse.Profile) {
        preferenceUtils.setUserFromPreference(user)
    }


    private fun checkConnection() {
        val mDialog = MaterialDialog.Builder(requireContext() as Activity)
            .setTitle("Oops")
            .setMessage("No internet connection")
            .setCancelable(true)
            .setAnimation(R.raw.inter2)
            .setPositiveButton(
                "Retry"
            ) { dialogInterface, which ->
                dialogInterface.dismiss()
                callUser()
            }
            .setNegativeButton(
                "Cancel"
            ) { dialogInterface, which ->
                dialogInterface.dismiss()

            }
            .build()
        mDialog.show()
    }

    private fun updateViews(offset: Float) {
        /* apply levels changes*/
        when (offset) {
            in 0.15F..1F -> {
                titleToolbarText.apply {
                    if (visibility != View.VISIBLE) visibility = View.VISIBLE
                    alpha = (1 - offset) * 0.35F
                }
            }

            in 0F..0.15F -> {
                titleToolbarText.alpha = (1f)
                ivUserAvatar.alpha = 1f
            }
        }

        /** collapse - expand switch*/
        when {
            offset < SWITCH_BOUND -> Pair(TO_EXPANDED, cashCollapseState?.second ?: WAIT_FOR_SWITCH)
            else -> Pair(TO_COLLAPSED, cashCollapseState?.second ?: WAIT_FOR_SWITCH)
        }.apply {
            when {
                cashCollapseState != null && cashCollapseState != this -> {
                    when (first) {
                        TO_EXPANDED -> {
                            /* set avatar on start position (center of parent frame layout)*/
                            ivUserAvatar.translationX = 0F
                            /**/
                            titleToolbarText.text = username
                            background.setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.color_transparent
                                )
                            )
                            /* hide top titles on toolbar*/
                            titleToolbarTextSingle.visibility = View.INVISIBLE
                        }
                        TO_COLLAPSED -> background.apply {
                            alpha = 0F
                            setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.white
                                )
                            )
                            animate().setDuration(250).alpha(1.0F)

                            textAvatar.textSize = 25f

                            /* show titles on toolbar with animation*/
                            titleToolbarTextSingle.apply {
                                visibility = View.VISIBLE
                                text = username
                                alpha = 0F
                                animate().setDuration(500).alpha(1.0f)
                            }
                        }
                    }
                    cashCollapseState = Pair(first, SWITCHED)
                }
                else -> {
                    cashCollapseState = Pair(first, WAIT_FOR_SWITCH)
                }
            }

            /* Collapse avatar img*/
            ivUserAvatar.apply {
                when {
                    offset > avatarAnimateStartPointY -> {
                        val avatarCollapseAnimateOffset =
                            (offset - avatarAnimateStartPointY) * avatarCollapseAnimationChangeWeight
                        val avatarSize =
                            EXPAND_AVATAR_SIZE - (EXPAND_AVATAR_SIZE - COLLAPSE_IMAGE_SIZE) * avatarCollapseAnimateOffset
                        this.layoutParams.also {
                            it.height = Math.round(avatarSize)
                            it.width = Math.round(avatarSize)
                        }
                        invisibleTextViewWorkAround.setTextSize(TypedValue.COMPLEX_UNIT_PX, offset)

                        this.translationX =
                            ((appBarLayout.width - horizontalToolbarAvatarMargin - avatarSize) / 2) * avatarCollapseAnimateOffset
                        this.translationY =
                            ((toolbar.height - verticalToolbarAvatarMargin - avatarSize) / 2) * avatarCollapseAnimateOffset
                    }
                    else -> this.layoutParams.also {
                        if (it.height != EXPAND_AVATAR_SIZE.toInt()) {
                            it.height = EXPAND_AVATAR_SIZE.toInt()
                            it.width = EXPAND_AVATAR_SIZE.toInt()
                            this.layoutParams = it
                        }
                        translationX = 0f
                    }
                }
            }
        }

    }

    override fun onItemClicked(_homeMeme: Meme_World) {

        val bundle = bundleOf(
            "id" to _homeMeme._id,
            "lastUpdated" to _homeMeme.lastUpdated,
            "likedBy" to _homeMeme.likedBy,
            "likes" to _homeMeme.likes,
            "placeholders" to _homeMeme.placeholders,
            "numPlaceholders" to _homeMeme.templateId.numPlaceholders,
            "tags" to _homeMeme.tags,
            "users" to _homeMeme.users,
            "templateIdCoordinates" to _homeMeme.templateId.coordinates,
            "imageUrl" to _homeMeme.templateId.imageUrl,
            "imageTags" to _homeMeme.templateId.tags,
            "imageName" to _homeMeme.templateId.name,
            "textSize" to _homeMeme.templateId.textSize,
            "textColor" to _homeMeme.templateId.textColorCode
        )

        val i = Intent(activity, CompletedMemeActivity::class.java)
        i.putExtra("bundle", bundle)
        startActivity(i)

    }
}