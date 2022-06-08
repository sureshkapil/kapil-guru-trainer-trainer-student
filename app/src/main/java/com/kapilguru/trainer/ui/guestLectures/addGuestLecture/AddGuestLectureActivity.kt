package com.kapilguru.trainer.ui.guestLectures.addGuestLecture

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityAddGuestLectureBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.guestLectures.GuestLecturesNewActivity
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data.AddGuestLectureRequest
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.viewModel.AddGuestLectureViewModel
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.viewModel.AddGuestLectureViewModelFactory
import com.kapilguru.trainer.ui.guestLectures.model.GuestLectureData
import com.kapilguru.trainer.ui.webiner.BottomSheetDialogFragmentInfo
import com.kofigyan.stateprogressbar.StateProgressBar

class AddGuestLectureActivity : BaseActivity() {
    private val TAG = "AddGuestLectureActivity"
    lateinit var binding: ActivityAddGuestLectureBinding
    lateinit var viewModel: AddGuestLectureViewModel
    lateinit var addGuestLecturePagerAdapter: AddGuestLecturePagerAdapter
    lateinit var dialog: CustomProgressDialog
    lateinit var infoBottomSheet: BottomSheetDialogFragmentInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_guest_lecture)
        viewModel = ViewModelProvider(this, AddGuestLectureViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application)).get(AddGuestLectureViewModel::class.java)
        checkAndGetIntentData()
        dialog = CustomProgressDialog(this)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setCustomActionBarListener()
        observeViewModelData()
        setGuestLecturePagerAdapter()
        setClickListeners()
    }

    private fun checkAndGetIntentData() {
        if (intent != null && intent.hasExtra("guestLectureData")) {
            val guestLectureData = intent.getSerializableExtra("guestLectureData") as GuestLectureData
            viewModel.setIsLaunchedForEdit(true)
            viewModel.addGuestLectureRequest = AddGuestLectureRequest().getAddGuestLectureReqFromGuestLectureData(guestLectureData)
            setData()
        }
    }

    private fun setData() {
        viewModel.noOfAttendeesMutLiveData.value = viewModel.addGuestLectureRequest.noOfAttendees.toString()
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.demo_lectures), true, object : BaseActivity.ToolBarClickListener {
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

    private fun observeViewModelData() {
        observeAddGuestLectureDetailsAPiResponse()
        observeGuestLectureImageApiResponse()
        observeGuestLectureVideoResponse()
        observeUpdateGuestLectureApiResponse()
        observeEditGuestLectureResponse()
        observeVideoUploadResponse()
    }

    private fun observeAddGuestLectureDetailsAPiResponse() {
        viewModel.addGuestLectureDetailsApiResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    viewModel.addGuestLectureRequest.isApiRequestMade = true
                    viewModel.addGuestLectureDetailsResData = it.data?.addGuestLectureResData!!
                    navigateToNextFragment()
                    dialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeGuestLectureImageApiResponse() {
        viewModel.addGuestLectureImageApiResponse.observe(this, Observer { addGuestLectureImageAPiRes ->
            when (addGuestLectureImageAPiRes.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    viewModel.onImageUploaded(true)
                    dialog.dismissLoadingDialog()
                    navigateToNextFragment()
                }
                Status.ERROR -> {
                    viewModel.onImageUploaded(false)
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeGuestLectureVideoResponse() {
        /*  viewModel.addGuestLectureVideoApiResponse.observe(this, Observer {
              when (it.status) {
                  Status.LOADING -> {
                      dialog.showLoadingDialog()
                  }
                  Status.SUCCESS -> {
                      viewModel.onVideoUploaded(true)
                      navigateToGuestLectureList()
                      dialog.dismissLoadingDialog()
                  }
                  Status.ERROR -> {
                      viewModel.onVideoUploaded(false)
                      dialog.dismissLoadingDialog()
                  }
              }
          })*/
    }

    private fun observeUpdateGuestLectureApiResponse() {
        viewModel.updateGuestLectureApiResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    navigateToGuestLectureList()
                    dialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeEditGuestLectureResponse() {
        viewModel.editGuestLectureApiResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    navigateToGuestLectureList()
                    dialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })

    }

    private fun observeVideoUploadResponse() {
        viewModel.uploadVideoResponse.observe(this, Observer { apiResponse ->
            when (apiResponse.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    viewModel.onVideoUploaded(true)
                    updateVideoCode()
                }
                Status.ERROR -> {
                    viewModel.onVideoUploaded(false)
                    dialog.dismissLoadingDialog()
                    // show error message
                }

            }
        })
    }

    private fun updateVideoCode() {
        // making final call
        viewModel.saveGuestLecture()
    }

    private fun navigateToGuestLectureList() {
        val intent = Intent(this, GuestLecturesNewActivity::class.java)
        intent.action = GuestLecturesNewActivity.ACTION_LECTURE_ADDED
        startActivity(intent)
        finish()
    }

    private fun setGuestLecturePagerAdapter() {
        val fragmentManager = supportFragmentManager
        addGuestLecturePagerAdapter = AddGuestLecturePagerAdapter(fragmentManager, lifecycle)
        binding.addGuestLectureViewPager.offscreenPageLimit = 3
        binding.addGuestLectureViewPager.adapter = addGuestLecturePagerAdapter
        binding.addGuestLectureViewPager.isUserInputEnabled = false
    }

    private fun setClickListeners() {
        setNextPageClickListener()
        setPreviousPageClickListener()
    }

    private fun setNextPageClickListener() {
        binding.nextAddGuestLecturePager.setOnClickListener {
            if (binding.addGuestLectureViewPager.currentItem <= 2) {
                when (binding.addGuestLectureViewPager.currentItem) {
                    0 -> {
                        onNextClickedInDetailsFragment()
                    }
                    1 -> {
                        onNextClickedInImageFragment()
                    }
                    2 -> {
                        onNextClickedInVideoFragment()
                    }
                }
            }
        }
    }

    private fun setPreviousPageClickListener() {
        binding.previousAddGuestLecturePager.setOnClickListener {
            if (binding.addGuestLectureViewPager.currentItem <= 3) {
                if (binding.addGuestLectureViewPager.currentItem == 0) {
                    // do nothing
                } else {
                    navigateToPreviousFragment()
                }
            }
        }
    }

    private fun onNextClickedInDetailsFragment() {
        setAbout()
        viewModel.addValuesToGuestLecReqData()
        if (detailsValid()) {
            if (!viewModel.addGuestLectureRequest.isApiRequestMade && !viewModel.getIsLaunchedForEdit()) {
                viewModel.addGuestLectureDetails()
            } else {
                navigateToNextFragment()
            }
        }
    }

    private fun detailsValid(): Boolean {
        val addGuestLectureData = viewModel.addGuestLectureRequest
        when {
            addGuestLectureData.title.isNullOrBlank() -> {
                showToast("Please enter Guest Lecture Title")
                return false
            }
            addGuestLectureData.noOfAttendees == null -> {
                showToast("Please enter No of Attendees")
                return false
            }
            addGuestLectureData.noOfAttendees!! > 150 -> {
                showToast("No of Attendees should not to exceed 150")
                return false
            }
            addGuestLectureData.lectureDate.isNullOrBlank() -> {
                showToast("Please select Lecture Date")
                return false
            }
            addGuestLectureData.startTime.isNullOrBlank() -> {
                showToast("Please select Start Time")
                return false
            }
            addGuestLectureData.duration == 0 -> {
                showToast("Please select Duration")
                return false
            }
            addGuestLectureData.languages.isNullOrBlank() -> {
                showToast("Please select Languages")
                return false
            }
            addGuestLectureData.trainerName.isNullOrBlank() -> {
                showToast("Please enter Trainer Name")
                return false
            }
            addGuestLectureData.about.isNullOrBlank() -> {
                showToast("Please enter Description")
                return false
            }
        }
        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun onNextClickedInImageFragment() {
        val addGuestLectureImageFragment = supportFragmentManager.findFragmentByTag("f1")
        val imageAsBase64String: String? = (addGuestLectureImageFragment as AddGuestLectureImageFragment).getImageAsBase64()
        if (viewModel.getIsLaunchedForEdit()) {
            if (viewModel.getIsImageChanged()) {
                viewModel.addGuestLectureImage(imageAsBase64String!!)
            } else {
                navigateToNextFragment()
            }
        } else { // case : adding new webinar
            imageAsBase64String?.let { it ->
                if (viewModel.getIsImageChanged()) {
                    viewModel.addGuestLectureImage(it)
                } else {
                    navigateToNextFragment()
                }
            } ?: run {
                showToast("please upload Demo Lecture Image")
            }
        }
    }


    /*
    *  onEdit fetch details from addGuestLectureRequest
    *
    * */
    private fun onNextClickedInVideoFragment() {
        val myFragment = supportFragmentManager.findFragmentByTag("f2")
        val videoFile = (myFragment as AddGuestLectureVideoFragment).getUploadVideoPath()
        videoFile?.let {
            val pref = StorePreferences(application)
            viewModel.uploadVideo(
                it, trainerId = pref.trainerId.toString(), sourceId = if (viewModel.getIsLaunchedForEdit()) {
                    viewModel.addGuestLectureRequest.id.toString()
                } else {
                    viewModel.addGuestLectureDetailsResData.id.toString()
                }, code = if (viewModel.getIsLaunchedForEdit()) {
                    viewModel.addGuestLectureRequest.code.toString()
                } else {
                    viewModel.addGuestLectureDetailsResData.code.toString()
                }, videoType = "Lectures"
            )
        } ?: run {
            // since no file uploaded yet we can save
            viewModel.saveGuestLecture()
        }
    }

    private fun setAbout() {
        val myFragment = supportFragmentManager.findFragmentByTag("f0")
        val about = (myFragment as AddGuestLectureDeatilsFragment).getDataFromRichTextEditor()
        about?.let {
            viewModel.addGuestLectureRequest.about = it.toBase64()
        }
    }

    private fun changeStepperState(currentPosition: Int) {
        when (currentPosition) {
            0 -> binding.stepperStateProgressBarAddGuestLecture.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)
            1 -> binding.stepperStateProgressBarAddGuestLecture.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
            2 -> binding.stepperStateProgressBarAddGuestLecture.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
        }
    }

    fun navigateToNextFragment() {
        viewModel.currentIndex.value = viewModel.currentIndex.value as Int + 1
        binding.addGuestLectureViewPager.setCurrentItem(viewModel.currentIndex.value as Int, true)
        changeStepperState(viewModel.currentIndex.value as Int)
    }

    fun navigateToPreviousFragment() {
        viewModel.currentIndex.value = viewModel.currentIndex.value as Int - 1
        binding.addGuestLectureViewPager.setCurrentItem(viewModel.currentIndex.value as Int, true)
        changeStepperState(viewModel.currentIndex.value as Int)
    }
}