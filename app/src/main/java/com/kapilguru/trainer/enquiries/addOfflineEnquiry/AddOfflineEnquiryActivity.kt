package com.kapilguru.trainer.enquiries.addOfflineEnquiry

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.MyApplication.Companion.context
import com.kapilguru.trainer.databinding.ActivityAddOfflineEnquiryBinding
import com.kapilguru.trainer.enquiries.addOfflineEnquiry.viewModel.AddOfflineEnquiryViewModel
import com.kapilguru.trainer.enquiries.addOfflineEnquiry.viewModel.AddOfflineEnquiryViewModelFactory
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.AddGuestLectureCourseAdapter

class AddOfflineEnquiryActivity : BaseActivity() {
    val TAG = "AddOfflineEnquiryFragment"
    private lateinit var binding: ActivityAddOfflineEnquiryBinding
    private lateinit var progressDialog: CustomProgressDialog
    private lateinit var addGuestLectureCourseAdapter: AddGuestLectureCourseAdapter
    private lateinit var viewModel: AddOfflineEnquiryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLateInitVariables()
        setCustomActionBarListener()
        observeViewModelData()
        setClickListeners()
        viewModel.getCourseList()
    }

    private fun initLateInitVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_offline_enquiry)
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(this, AddOfflineEnquiryViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application)).get(AddOfflineEnquiryViewModel::class.java)
        binding.viewModel = viewModel
        addGuestLectureCourseAdapter = AddGuestLectureCourseAdapter(this)
        binding.spinnerCourse.adapter = addGuestLectureCourseAdapter
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.enquiries))
    }

    private fun observeViewModelData() {
        observeLoadingIndicatorLivedata()
        observeInformUser()
        observeCourseListLivedata()
    }

    private fun observeLoadingIndicatorLivedata() {
        viewModel.showLoadingIndicator.observe(this, Observer { shouldShow ->
            if (shouldShow) {
                progressDialog.showLoadingDialog()
            } else {
                progressDialog.dismissLoadingDialog()
            }
        })
    }

    private fun observeInformUser() {
        viewModel.informUser.observe(this, Observer { message ->
            showAppToast(context, message)
        })
    }

    private fun observeCourseListLivedata() {
        viewModel.courseListMutLiveData.observe(this, Observer { courseList ->
            if (courseList.isNotEmpty()) {
                addGuestLectureCourseAdapter.setCategoryList(courseList)
            }
        })
    }

    private fun setSpinnerCourseItemSelectListener() {
        binding.spinnerCourse.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCourseId = viewModel.courseListMutLiveData.value?.get(position)?.courseId
                viewModel.addOfflineEnquiryMutLiveData.value?.courseId = selectedCourseId.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setClickListeners() {
        setSpinnerCourseItemSelectListener()
        binding.btnSubmit.setOnClickListener {
            onSubmitClicked()
        }
    }

    private fun onSubmitClicked() {
        addRequestData()
        viewModel.checkAndAddEnquiry()
    }

    private fun addRequestData() {
        val trainerId = StorePreferences(this).userId
        val uuid = generateUuid().toString()
        val countryCode = StorePreferences(this).countryCode.toString()
        viewModel.addOfflineEnquiryMutLiveData.value?.countryCode = countryCode
        viewModel.addOfflineEnquiryMutLiveData.value?.uuid = uuid
        viewModel.addOfflineEnquiryMutLiveData.value?.trainerId = trainerId
        viewModel.addOfflineEnquiryMutLiveData.value?.createdBy = trainerId // -- ask wht we are setting created by, Is there any difference between created by and trainerid.
    }

    companion object {
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, AddOfflineEnquiryActivity::class.java)
            activity.startActivity(intent)
        }
    }
}