package com.kapilguru.trainer.ui.webiner.webinarDetailsActivity

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.MediaController
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityWebinarDetailsBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data.LanguageData
import com.kapilguru.trainer.ui.webiner.BottomSheetDialogFragmentInfo
import com.kapilguru.trainer.ui.webiner.model.ActiveWebinarData
import com.kapilguru.trainer.ui.webiner.webinarDetailsActivity.viewModel.WebinarDetailsViewModel
import com.kapilguru.trainer.ui.webiner.webinarDetailsActivity.viewModel.WebinarDetailsViewModelFactory
import com.kapilguru.trainer.videoCall.VideoCallInterfaceImplementation
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WebinarDetailsActivity : BaseActivity() {
    private val TAG = "WebinarDetailsActivity"
    lateinit var binding: ActivityWebinarDetailsBinding
    lateinit var viewModel: WebinarDetailsViewModel
    lateinit var progressDialog: CustomProgressDialog
    lateinit var infoBottomSheet: BottomSheetDialogFragmentInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeLateInitVariables()
        getIntentData()
        setCustomActionBarListener()
        observeViewModelData()
    }

    private fun observeViewModelData() {
        viewModel.webinarResponseData.observe(this,  {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    it.data?.webinarData?.let { webinarInfo->
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
        setGoLiveVisibility()
        setClickListeners()
        setVideo()
    }

    private fun initializeLateInitVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_webinar_details)
        viewModel = ViewModelProvider(this, WebinarDetailsViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(WebinarDetailsViewModel::class.java)
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
    }

    private fun getIntentData() {
        val webinarData: String? = intent.getStringExtra("webinarData")
        webinarData?.let {it->
            viewModel.fetchWebinarDetails(it)
        }
    }

    private fun setCustomActionBarListener() {
        val activityName = viewModel.webinarData.title + getString(R.string.webinar_details)
        setActionbarBackListener(this, binding.actionbar, activityName, true, object : BaseActivity.ToolBarClickListener {
            override fun onShowInfoClicked() {
                showInfo()
            }
        })
    }

    private fun showInfo() {
        val arrayList = java.util.ArrayList<String>()
        arrayList.add(getString(R.string.webinar_info_1))
        arrayList.add(getString(R.string.webinar_info_2))
        arrayList.add(getString(R.string.webinar_info_3))
        arrayList.add(getString(R.string.webinar_info_4))
        arrayList.add(getString(R.string.webinar_info_5))
        infoBottomSheet = BottomSheetDialogFragmentInfo.newInstance(arrayList, getString(R.string.conduct_webinars))
        infoBottomSheet.show(supportFragmentManager, "")
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

    private fun setGoLiveVisibility() {
        val isRejected = viewModel.webinarData.isRejected
        val isVerified = viewModel.webinarData.isVerified
        val shouldShow = checkAndSetGoLiveVisibility(isRejected, isVerified, viewModel.webinarData.startDate, viewModel.webinarData.endDate)
        if (shouldShow) {
            binding.btGoLive.visibility = View.VISIBLE
        } else {
            binding.btGoLive.visibility = View.GONE
        }
    }

    private fun setClickListeners() {
        binding.btGoLive.setOnClickListener {
            val webinarData = viewModel.webinarData
            val roomName = webinarData.roomName ?: "Room Name"
            val participantName = webinarData.speakerName ?: "Participant Name"
            val hostUserName = webinarData.speakerName ?: "Host User Name"
            VideoCallInterfaceImplementation.launchVideoCall(this, roomName, participantName, hostUserName)
        }
    }

    private fun setVideo() {
        viewModel.webinarData.webinarVideo?.let { videoCode->
            val videoUrl = VIDEO_BASE_URL + videoCode
            val uri: Uri = Uri.parse(videoUrl)
            binding.videoView.setVideoURI(uri)
            val mediaController = MediaController(this)
            mediaController.setAnchorView( binding.videoView)
            mediaController.setMediaPlayer(binding.videoView)
            binding.videoView.setMediaController(mediaController)
            binding.videoView.seekTo(100)
        }
    }

    companion object {
        const val TAG = "WebinarDetailsActivity"
       /* const val WEBINAR_ID = "WEBINAR_ID"
        const val IS_LAUNCHED_FROM_TODAYS_SCHEDULE = "IS_LAUNCHED_FROM_TODAYS_SCHEDULE"

        fun launchActivity(activity: Activity, webinarId: String) {
            val intent = Intent(activity, WebinarDetailsActivity::class.java)
            intent.putExtra(WEBINAR_ID, webinarId)
            intent.putExtra(IS_LAUNCHED_FROM_TODAYS_SCHEDULE, true)
            activity.startActivity(intent)
        }*/

        fun checkAndSetGoLiveVisibility(isRejected: Int, isVerified: Int, webinarStartDateString: String?, webinarEndDateString: String?): Boolean {
            if (isRejected == 1 || isVerified == 0) {
                return false
            }
            var isTenMinutesBeforeStart = false
            var isStarted = false
            var isDurationNotCompleted = false
            val inputTimeFormat = SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITH_T)
            inputTimeFormat.timeZone = TimeZone.getTimeZone("GMT")
            val currentDate = Calendar.getInstance()
            val webinarStartDateCalendar = Calendar.getInstance()
            val webinarEndDateCalendar = Calendar.getInstance()
            webinarStartDateString?.let { startTimeNotNull ->
                webinarStartDateCalendar.time = inputTimeFormat.parse(startTimeNotNull)
                webinarStartDateCalendar.set(Calendar.YEAR, currentDate.get(Calendar.YEAR))
                webinarStartDateCalendar.set(Calendar.MONTH, currentDate.get(Calendar.MONTH))
                webinarStartDateCalendar.set(Calendar.DAY_OF_MONTH, currentDate.get(Calendar.DAY_OF_MONTH))
                val timeDiffInMillis = webinarStartDateCalendar.timeInMillis - currentDate.timeInMillis
                isTenMinutesBeforeStart = timeDiffInMillis <= 10 * 60 * 1000 && timeDiffInMillis > 0
                if (timeDiffInMillis < 0) {
                    isStarted = true
                }
            }
            if (isStarted) {
                webinarEndDateString?.let { endDateNotNull ->
                    webinarEndDateCalendar.time = inputTimeFormat.parse(endDateNotNull)
                    webinarEndDateCalendar.set(Calendar.YEAR, currentDate.get(Calendar.YEAR))
                    webinarEndDateCalendar.set(Calendar.MONTH, currentDate.get(Calendar.MONTH))
                    webinarEndDateCalendar.set(Calendar.DAY_OF_MONTH, currentDate.get(Calendar.DAY_OF_MONTH))
                    isDurationNotCompleted = webinarEndDateCalendar.timeInMillis >= currentDate.timeInMillis
                }
            }
            return isTenMinutesBeforeStart || isDurationNotCompleted
        }
    }
}