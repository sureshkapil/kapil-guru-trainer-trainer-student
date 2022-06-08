package com.kapilguru.trainer.ui.guestLectures.guestLectureStudent

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityGuestLectureStudentViewBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data.LanguageData
import com.kapilguru.trainer.ui.guestLectures.guestLectureStudent.viewModel.GuestLectureStudentViewViewModel
import com.kapilguru.trainer.ui.guestLectures.guestLectureStudent.viewModel.GuestLectureStudentViewViewModelFactory
import java.util.*

class GuestLectureStudentViewActivity : AppCompatActivity() {
    private val TAG = "GuestLectureStudentViewActivity"
    lateinit var binding: ActivityGuestLectureStudentViewBinding
    lateinit var viewModel: GuestLectureStudentViewViewModel
    lateinit var progressDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_guest_lecture_student_view)
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(this, GuestLectureStudentViewViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(GuestLectureStudentViewViewModel::class.java)
        val demoLecture = intent.getIntExtra(ACTIVE_DEMO_LECTURE, -1)
        viewModel.demoLectureIntent = demoLecture
        binding.viewModel = viewModel
        setActivityName()
        observeViewModelData()
        fetchDemoLectureDetails()
        setClickListeners()
    }

    private fun setActivityName() {
        binding.customActionBar.actvActivityName.text = getString(R.string.demo_lecture)
    }

    private fun fetchDemoLectureDetails() {
        viewModel.getDemoLectureDetails()
    }

    private fun setClickListeners() {
        binding.customActionBar.acivBack.setOnClickListener {
            finish()
        }
    }

    private fun observeViewModelData() {
        observeDemoLectureDetailsApiRes()
    }

    private fun observeDemoLectureDetailsApiRes() {
        viewModel.demoLectureDetailsApiRes.observe(this, Observer { webinarDetailsApiRes ->
            when (webinarDetailsApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    webinarDetailsApiRes.data?.let { demolectureDetailsApiRes ->
                        demolectureDetailsApiRes.demoLectureDataList?.let { demoLectureResData ->
                            try {
                                viewModel.demoLectureDetailsApi.value = demoLectureResData[0]
                                fetchAndSetLanguages()
                                setVideo()
                            } catch (exception: IndexOutOfBoundsException) {

                            }
                            progressDialog.dismissLoadingDialog()
                        }
                    }
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun fetchAndSetLanguages() {
        val selectedLanguagesIdList = viewModel.demoLectureDetailsApi.value?.languages?.fromBase64()
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
        viewModel.demoLectureDetailsApi.value?.lectureVideo?.let { code ->
            val videoUrl = VIDEO_BASE_URL + code
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
        val ACTIVE_DEMO_LECTURE = "ACTIVE_DEMO_LECTURE"

        fun launchActivity(activity: Activity, activeDemoLectures: Int) {
            val intent = Intent(activity, GuestLectureStudentViewActivity::class.java)
            val bundle = Bundle()
            bundle.putInt(ACTIVE_DEMO_LECTURE, activeDemoLectures)
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }
    }
}