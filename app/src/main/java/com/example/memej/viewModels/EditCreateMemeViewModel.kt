package com.example.memej.viewModels

import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.models.body.edit.EditMemeBody
import com.example.memej.models.responses.edit.EditMemeApiResponse
import com.example.memej.models.responses.home.HomeMemeApiResponse
import retrofit2.Call
import retrofit2.Response

class EditCreateMemeViewModel : ViewModel() {

    val TAG = EditCreateMemeViewModel::class.java.simpleName


    //For exiting the workspace
    var successfulEdit: MutableLiveData<Boolean> = MutableLiveData()
    var messageEdit: MutableLiveData<String> = MutableLiveData()

    var successfulTag: MutableLiveData<Boolean> = MutableLiveData()
    var messageTag: MutableLiveData<String> = MutableLiveData()

    var successfulCreate: MutableLiveData<Boolean> = MutableLiveData()
    var messageCreate: MutableLiveData<String> = MutableLiveData()

    private val context = ApplicationUtil.getContext()
    private val sessionManager = SessionManager(context)
    private val service = RetrofitClient.makeCallsForMemes(context)

    private var editMemeResponse: MutableLiveData<EditMemeApiResponse> = MutableLiveData()
    private var tagsResponse: MutableLiveData<HomeMemeApiResponse> = MutableLiveData()
    private var createMemeResponse: MutableLiveData<HomeMemeApiResponse> = MutableLiveData()

    fun edit(
        body: EditMemeBody,
        pb: ProgressBar,
        mSnackbar: View
    ): MutableLiveData<EditMemeApiResponse> {
        editMemeResponse = editFunction(body, pb, mSnackbar)
        return editMemeResponse
    }

    private fun editFunction(
        body: EditMemeBody,
        pb: ProgressBar,
        mSnackbar: View
    ): MutableLiveData<EditMemeApiResponse> {

        service.editMeme(
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}",
            info = body
        )
            .enqueue(object : retrofit2.Callback<EditMemeApiResponse> {
                override fun onFailure(call: Call<EditMemeApiResponse>, t: Throwable) {

                    successfulEdit.value = false
                    messageEdit.value = ErrorStatesResponse.returnStateMessageForThrowable(t)
                    ErrorStatesResponse.logThrowables(t, TAG)
                    pb.visibility = View.GONE
                    ErrorStatesResponse.createSnackbar(messageEdit.value, mSnackbar)

                }

                override fun onResponse(
                    call: Call<EditMemeApiResponse>,
                    response: Response<EditMemeApiResponse>
                ) {
                    //Response will be good if the meme is created

                }
            })


        return editMemeResponse
    }


}