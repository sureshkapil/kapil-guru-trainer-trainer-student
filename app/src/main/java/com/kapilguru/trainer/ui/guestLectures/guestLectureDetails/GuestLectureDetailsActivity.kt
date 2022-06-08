package com.kapilguru.trainer.ui.guestLectures.guestLectureDetails

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.MediaController
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityGuestLectureDetailsBinding
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data.LanguageData
import com.kapilguru.trainer.ui.guestLectures.guestLectureDetails.viewModel.GuestLectureDetailsViewModel
import com.kapilguru.trainer.ui.guestLectures.guestLectureDetails.viewModel.GuestLectureDetailsViewModelFactory
import com.kapilguru.trainer.ui.guestLectures.model.GuestLectureData
import com.kapilguru.trainer.ui.webiner.BottomSheetDialogFragmentInfo
import com.kapilguru.trainer.videoCall.VideoCallInterfaceImplementation
import java.text.SimpleDateFormat
import java.util.*

class GuestLectureDetailsActivity : BaseActivity() {
    private val TAG = "GuestLecDetailsAct"
    lateinit var binding: ActivityGuestLectureDetailsBinding
    lateinit var viewModel: GuestLectureDetailsViewModel
    lateinit var progressDialog: CustomProgressDialog
    lateinit var infoBottomSheet: BottomSheetDialogFragmentInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeLateInitVariables()
        setCustomActionBarListener()
        getIntentData()
        fetchAndSetLanguages()
        setGoLiveVisibility()
        setClickListeners()
        setVideo()
    }

    private fun setGoLiveVisibility() {
        val isRejected = viewModel.guestLectureDetails.isRejected
        val isVerified = viewModel.guestLectureDetails.isVerified
       val shouldShow = checkGoLiveVisibility(isRejected,isVerified,viewModel.guestLectureDetails.lectureDate,viewModel.guestLectureDetails.duration)
        if(shouldShow){
            binding.btGoLive.visibility = View.VISIBLE
        }else{
            binding.btGoLive.visibility = View.GONE
        }
//        binding.btGoLive.visibility = View.VISIBLE//---remove
    }

    private fun setVideo() {
//        val videoUrl = "https://bridge01.videomeet.in/filedownload.php?filepath=L29wdC9qaWJyaS9yZWNvcmRpbmdzL2p5Y3F3anNkanh6Y3p0a28vMTY0MDE2NTA0ODk3NWJ0MTY5OTFfMjAyMS0xMi0yMy0yMC0zNi0zNy5tcDQ="
//        val videoUrl = "https://media.geeksforgeeks.org/wp-content/uploads/20201217192146/Screenrecorder-2020-12-17-19-17-36-828.mp4?_=1"
        viewModel.guestLectureDetails.lectureVideo?.let { videoCode ->
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

    private fun initializeLateInitVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_guest_lecture_details)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, GuestLectureDetailsViewModelFactory()).get(GuestLectureDetailsViewModel::class.java)
    }

    private fun getIntentData() {
        val guestLectureData = intent.getSerializableExtra("guestLectureData") as GuestLectureData
        viewModel.guestLectureDetails = guestLectureData
        binding.guestLectureDetailsView = guestLectureData
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.add_webinar), true, object : BaseActivity.ToolBarClickListener {
            override fun onShowInfoClicked() {
                showInfo()
            }
        })
    }

    private fun showInfo() {
        val arrayList = java.util.ArrayList<String>()
        arrayList.add(getString(R.string.demo_lecture_info_1))
        arrayList.add(getString(R.string.demo_lecture_info_2))
        arrayList.add(getString(R.string.demo_lecture_info_3))
        arrayList.add(getString(R.string.demo_lecture_info_4))
        arrayList.add(getString(R.string.demo_lecture_info_5))
        infoBottomSheet = BottomSheetDialogFragmentInfo.newInstance(arrayList, getString(R.string.schedule_demo_lecture))
        infoBottomSheet.show(supportFragmentManager, "")
    }

    private fun fetchAndSetLanguages() {
        val selectedLanguagesIdList = viewModel.guestLectureDetails.languages?.fromBase64()
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

    private fun setClickListeners() {
        binding.btGoLive.setOnClickListener {
            val guestLectureData = viewModel.guestLectureDetails
            val roomName = guestLectureData.roomName ?: "Room Name"
            val participantName = guestLectureData.trainerName ?: "Participant Name"
            val hostUserName = guestLectureData.trainerName ?: "Host User Name"
            VideoCallInterfaceImplementation.launchVideoCall(this, roomName, participantName, hostUserName)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {

        fun checkGoLiveVisibility(isRejected: Int, isVerified: Int,guestLectureStartDateString: String?, duration: Int?): Boolean {
            if (isRejected == 1 || isVerified == 0) {
                return false
            }
            var guestLectureDuration = 0
            if(duration != null){
                guestLectureDuration = duration
            }
            var isTenMinutesBeforeStart = false
            var isStarted = false
            var isDurationNotCompleted = false
            val inputTimeFormat = SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITH_T)
            inputTimeFormat.timeZone = TimeZone.getTimeZone("GMT")
            val currentDate = Calendar.getInstance()
            val guestLectureStartDateCalendar = Calendar.getInstance()
            guestLectureStartDateString?.let { startTimeNotNull ->
                guestLectureStartDateCalendar.time = inputTimeFormat.parse(startTimeNotNull)
                val timeDiffInMillis = guestLectureStartDateCalendar.timeInMillis - currentDate.timeInMillis
                if(timeDiffInMillis < 0){
                    isStarted = true
                }
                isTenMinutesBeforeStart = timeDiffInMillis <= 10 * 60 * 1000 && timeDiffInMillis > 0
                if(isStarted){
                    val durationInMillis = guestLectureDuration * 60 * 1000
                    if(currentDate.timeInMillis<=guestLectureStartDateCalendar.timeInMillis+durationInMillis){
                        isDurationNotCompleted = true
                    }
                }
            }
            return isTenMinutesBeforeStart || isDurationNotCompleted
        }
    }
}