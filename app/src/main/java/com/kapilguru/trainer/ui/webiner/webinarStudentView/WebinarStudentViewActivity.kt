package com.kapilguru.trainer.ui.webiner.webinarStudentView

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityWebinarStudentViewBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data.LanguageData
import com.kapilguru.trainer.ui.webiner.model.ActiveWebinarData
import com.kapilguru.trainer.ui.webiner.webinarDetailsActivity.viewModel.WebinarDetailsViewModel
import com.kapilguru.trainer.ui.webiner.webinarDetailsActivity.viewModel.WebinarDetailsViewModelFactory

class WebinarStudentViewActivity : AppCompatActivity() {
    private val TAG = "WebinarStudentViewActivity"
    lateinit var binding: ActivityWebinarStudentViewBinding
    lateinit var viewModel: WebinarDetailsViewModel
    lateinit var progressDialog: CustomProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_webinar_student_view)
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(this, WebinarDetailsViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(WebinarDetailsViewModel::class.java)
        setActivityName()
        getIntentData()
        setClickListeners()
        observeViewModelData()
    }

    private fun getIntentData() {
        val webinarId: Int = intent.getIntExtra(KEY_WEBINAR_ID,-1)
        if(webinarId != -1){
            viewModel.fetchWebinarDetails(webinarId.toString())
        }
    }

    private fun setActivityName() {
        binding.customActionBar.actvActivityName.text = getString(R.string.demo_lecture)
    }

    private fun setClickListeners() {
        binding.customActionBar.acivBack.setOnClickListener {
            finish()
        }
    }

    private fun observeViewModelData() {
        viewModel.webinarResponseData.observe(this, {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    it.data?.webinarData?.let { webinarInfo ->
                        setData(webinarInfo)
                    }
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun setData(it: ArrayList<ActiveWebinarData>) {
        viewModel.webinarData = it[0]
        binding.webinarDetailsView = it[0]
        fetchAndSetLanguages()
        setVideo()
    }

    private fun fetchAndSetLanguages() {
        val selectedLanguagesIdList = viewModel.webinarData.languages?.fromBase64()
        val languagesList = MutableLiveData<ArrayList<LanguageData>>()
        languagesList.observe(this, androidx.lifecycle.Observer { languagesListNotNull ->
            selectedLanguagesIdList?.let { selectedLanguagesIdListNotNull ->
                val selectedLanguages = getSelectedLanguagesStringToShow(languagesListNotNull, selectedLanguagesIdListNotNull)
                binding.actvLanguagesValue.text = selectedLanguages
            }
        })
        (application as MyApplication).getLanguages(languagesList)
    }

    private fun getSelectedLanguagesStringToShow(languagesList: ArrayList<LanguageData>, selectedLanguageIds: String): String {
        val selectedLanguageNames = StringBuilder()
        val selectedIds = selectedLanguageIds.split(",")
        for (i in selectedIds) {
            for (language in languagesList) {
                if (TextUtils.equals(i, language.id.toString())) {
                    selectedLanguageNames.append(language.name + " , ")
                    break
                }
            }
        }
        return selectedLanguageNames.toString().substring(0, selectedLanguageNames.length - 2)
    }

    private fun setVideo() {
        viewModel.webinarData.webinarVideo?.let { videoCode ->
            val videoUrl = VIDEO_BASE_URL + videoCode
            val uri: Uri = Uri.parse(videoUrl)
            binding.videoView.setVideoURI(uri)
            val mediaController = MediaController(this)
            mediaController.setAnchorView(binding.videoView)
            mediaController.setMediaPlayer(binding.videoView)
            binding.videoView.setMediaController(mediaController)
            binding.videoView.seekTo(100)
        }
    }

    companion object {
        const val KEY_WEBINAR_ID = "WEBINAR_ID"

        fun launchActivity(webinarId: Int, activity: Activity) {
            val intent = Intent(activity, WebinarStudentViewActivity::class.java).apply {
                putExtra(KEY_WEBINAR_ID, webinarId)
            }
            activity.startActivity(intent)
        }
    }
}