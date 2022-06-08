package com.kapilguru.trainer.ui.webiner.addWebinar

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityAddWebinarBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.webiner.BottomSheetDialogFragmentInfo
import com.kapilguru.trainer.ui.webiner.WebinarNewActivity
import com.kapilguru.trainer.ui.webiner.addWebinar.viewModel.AddWebinarViewModel
import com.kapilguru.trainer.ui.webiner.addWebinar.viewModel.AddWebinarViewModelFactory
import com.kofigyan.stateprogressbar.StateProgressBar
import kotlinx.android.synthetic.main.activity_add_course.*
import kotlinx.android.synthetic.main.activity_add_webinar.*
import java.util.*

class AddWebinarActivity : BaseActivity() {
    private val TAG = "AddWebinarActivity"
    lateinit var binding: ActivityAddWebinarBinding
    lateinit var viewModel: AddWebinarViewModel
    lateinit var addWebinarPageAdapter: AddWebinarPageAdapter
    lateinit var dialog: CustomProgressDialog
    lateinit var infoBottomSheet: BottomSheetDialogFragmentInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_webinar)
        viewModel = ViewModelProvider(this, AddWebinarViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application))
            .get(AddWebinarViewModel::class.java)
        binding.addWebinarModel = viewModel
        binding.lifecycleOwner = this
        setCustomActionBarListener()
        setWebinarPagerAdapter()
        dialog = CustomProgressDialog(this)
        getIntentData()
        setClickListeners()
        observeViewModelData()
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
        arrayList.add(getString(R.string.webinar_info_1))
        arrayList.add(getString(R.string.webinar_info_2))
        arrayList.add(getString(R.string.webinar_info_3))
        arrayList.add(getString(R.string.webinar_info_4))
        arrayList.add(getString(R.string.webinar_info_5))
        infoBottomSheet = BottomSheetDialogFragmentInfo.newInstance(arrayList, getString(R.string.conduct_webinars))
        infoBottomSheet.show(supportFragmentManager, "")
    }

    private fun setWebinarPagerAdapter() {
        val fragmentManager = supportFragmentManager
        addWebinarPageAdapter = AddWebinarPageAdapter(fragmentManager, lifecycle)
        binding.webinarViewPager.offscreenPageLimit = 3
        binding.webinarViewPager.adapter = addWebinarPageAdapter
        binding.webinarViewPager.isUserInputEnabled = false
    }

    private fun getIntentData() {
        val webinarKeyValue = intent.getIntExtra(WEBINAR_EDIT_KEY_PARAM, 0)
        val webinarId = intent.getIntExtra(WEBINAR_EDIT_ID_PARAM, 0)
        if (webinarKeyValue == 2) {
            viewModel.webinarEditId.value = webinarId
            viewModel.fetchWebinarDetails()
            viewModel.setIsLaunchedForEdit(true)
        } else if (webinarKeyValue == 1) {
            viewModel.webinarEditId.value = 0
            viewModel.setIsLaunchedForEdit(false)
        }
    }

    private fun setClickListeners() {
        setNextPageClickListener()
        setPreviousPageClickListener()
    }

    private fun setNextPageClickListener() {
        binding.nextWebinarPager.setOnClickListener {
            if (binding.webinarViewPager.currentItem <= 2) {
                when (viewModel.currentIndex.value) {
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

    private fun onNextClickedInDetailsFragment() {
        setAddWebinarRequestData()
        val myFragment = supportFragmentManager.findFragmentByTag("f" + webinarViewPager.currentItem)
        val startTime = (myFragment as AddWebinarDetailsFragment).binding.tietStartTime.text.toString()
        if (viewModel.isDataValid(startTime)) {
            if (!viewModel.addWebinarRequest.value?.isApiRequestMade!! && !viewModel.getIsLaunchedForEdit()) {
                viewModel.addWebinarDetails()
            } else {
                navigateToNextFragment()
            }
        }
    }

    private fun setAddWebinarRequestData() {
        setAbout()
        viewModel.addValuesToWebinarReqData()
    }

    private fun setAbout() {
        val myFragment = supportFragmentManager.findFragmentByTag("f" + webinarViewPager.currentItem)
        val webDescription = (myFragment as AddWebinarDetailsFragment).getDataFromRichTextEditor()
        viewModel.addWebinarRequest.value?.about = webDescription.toBase64()
    }

    private fun onNextClickedInImageFragment() {
        val webinarImageFragment = supportFragmentManager.findFragmentByTag("f1")
        val imageAsBase64String: String? = (webinarImageFragment as AddWebinarImageFragment).getImageAsBase64()
        if (viewModel.getIsLaunchedForEdit()) {
            if (viewModel.getIsNewImageSelected()) {
                viewModel.addWebinarImage(imageAsBase64String!!)
            } else {
                navigateToNextFragment()
            }
        } else { // case : adding new webinar
            imageAsBase64String?.let {
                if (viewModel.getIsNewImageSelected()) {
                    viewModel.addWebinarImage(it)
                } else {
                    navigateToNextFragment()
                }
            } ?: run {
                showErrorMessage("please upload course image")
            }
        }
    }

    private fun onNextClickedInVideoFragment() {
            val myFragment = supportFragmentManager.findFragmentByTag("f2")
            val videoFile = (myFragment as AddWebinarVideoFragment).getUploadVideoPath()
            videoFile?.let{
                val pref = StorePreferences(application)
                viewModel.uploadVideo(it,  trainerId = pref.trainerId.toString(),
                    sourceId = viewModel.addWebinarDetailsResData.id.toString(),
                    code = viewModel.addWebinarDetailsResData.code.toString(),
                    videoType = "Webinar")
            }?:run{
                // since no file uploaded yet we can save
                viewModel.saveWebinar()
            }
    }

    private fun setPreviousPageClickListener() {
        binding.previousWebinarPager.setOnClickListener {
            if (binding.webinarViewPager.currentItem <= 2) {
                if (binding.webinarViewPager.currentItem == 0) {
                    return@setOnClickListener
                } else {
                    viewModel.currentIndex.value = viewModel.currentIndex.value as Int - 1
                    binding.webinarViewPager.setCurrentItem(viewModel.currentIndex.value as Int, true)
                    changeStepperState(viewModel.currentIndex.value as Int)
                }
            }
        }
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun observeViewModelData() {
        observeAddWebinarDetailsAPiResponse()
        observeWebinarImageApiResponse()
        observeWebinarVideoResponse()
        observeUpdateWebinarApiResponse()
        observeEditWebinarApiResponse()
    }

    private fun observeAddWebinarDetailsAPiResponse() {
        viewModel.addWebinarDetailsApiResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    viewModel.addWebinarRequest.value?.isApiRequestMade = true
                    viewModel.addWebinarDetailsResData = it.data?.addWebinarDetailsResData!!
                    navigateToNextFragment()
                    dialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeWebinarImageApiResponse() {
        viewModel.addWebinarImageApiResponse.observe(this, Observer { addWebinarImageApiRes ->
            when (addWebinarImageApiRes.status) {
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

    private fun observeWebinarVideoResponse() {
        viewModel.addWebinarVideoApiResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    viewModel.onVideoUploaded(true)
                    updateVideoCode()
//                    navigateToWebinarList()
                    dialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    viewModel.onVideoUploaded(false)
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun updateVideoCode() {
        // making final call
        viewModel.saveWebinar()
    }

    private fun observeUpdateWebinarApiResponse() {
        viewModel.updateWebinarApiResponse.observe(this, Observer { apiResponse ->
            when (apiResponse.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    apiResponse.data?.commonResponse?.let { it ->
                        navigateToWebinarList()
                    }
                    dialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }

            }
        })
    }

    private fun observeEditWebinarApiResponse(){
        viewModel.updateEditWebinarApi.observe(this, Observer { apiResponse ->
            when (apiResponse.status) {
                Status.SUCCESS -> {
                    apiResponse.data?.commonResponse?.let { it ->
                        navigateToWebinarList()
                    }
                    dialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
            }
        })
    }

    private fun navigateToWebinarList() {
        val intent = Intent(this, WebinarNewActivity::class.java)
        intent.action = WebinarNewActivity.ACTION_WEBINAR_ADDED
        startActivity(intent)
        finish()
    }

    private fun changeStepperState(currentPosition: Int) {
        when (currentPosition) {
            0 -> stepperStateProgressBarWebinar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)
            1 -> stepperStateProgressBarWebinar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
            2 -> stepperStateProgressBarWebinar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
        }
    }

    fun navigateToNextFragment() {
        viewModel.currentIndex.value = viewModel.currentIndex.value as Int + 1
        binding.webinarViewPager.setCurrentItem(viewModel.currentIndex.value as Int, true)
        changeStepperState(viewModel.currentIndex.value as Int)
    }
}