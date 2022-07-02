package com.kapilguru.trainer.faculty.addFaculty

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityAddFacultyBinding
import com.kapilguru.trainer.faculty.*
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences

class AddFacultyActivity : BaseActivity() {

    lateinit var binding: ActivityAddFacultyBinding
    lateinit var viewModel: FacultyViewModel
    lateinit var dialog: CustomProgressDialog
    var isFromEdit: Boolean? = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_faculty)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_faculty)
        viewModel = ViewModelProvider(this, FacultyViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(FacultyViewModel::class.java)
        binding.viewModel = viewModel
        dialog = CustomProgressDialog(this)
        binding.lifecycleOwner = this
        setCustomActionBarListener()
        setClickListeners()
        getIntentData()
        observeViewModel()
    }

    private fun setClickListeners() {
        val trainerId = StorePreferences(this).userId
        val tenantId = StorePreferences(this).tenantId
        binding.addFaculty.setOnClickListener {
            isFromEdit?.let {
                if (it) {
                    viewModel.updateFaculty(viewModel.updateFacultyId!!, viewModel.facultySettingsModelApi.value!!)
                } else {
                    if (viewModel.validateAddFaculty()) {
                        viewModel.addFaculty(trainerId, tenantId)
                    }
                }
            }
        }
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.add_faculty))
    }

    private fun getIntentData() {
        isFromEdit = intent?.getBooleanExtra(IS_FROM_EDIT_PARAM, false)
        isFromEdit?.let { it ->
            if (it) {
                viewModel.isFromEdit.value = it
                viewModel.facultySettingsModelApi.value = intent?.getParcelableExtra<FacultySettingsModel>(Faculty_Settings_PARAM)
                viewModel.addFacultyRequest.value = intent?.getParcelableExtra<AddFacultyRequest>(ADD_FACULTY_REQUEST_PARAM)
                viewModel.updateFacultyId = intent?.getStringExtra(UPDATE_FACULTY_ID)
                binding.addFaculty.setText("Update Faculty")
            }
        }
    }

    private fun observeViewModel() {
        viewModel.addFacultyResponse.observe(this, androidx.lifecycle.Observer { it ->
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                    /* this api call is made after another api call.
                     So no need to show the loading indicator as it is shown already.*/
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    navigateToFaculty("Trainer Added Successfully")
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })

        viewModel.facultySettingsModel.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                    /* this api call is made after another api call.
                     So no need to show the loading indicator as it is shown already.*/
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    it?.data?.let { response ->
                        viewModel.facultySettingsModelApi.value = response
                    }

                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })

        viewModel.updateFacultySettingsResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                    /* this api call is made after another api call.
                     So no need to show the loading indicator as it is shown already.*/
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    navigateToFaculty("Trainer Details Updated")

                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun navigateToFaculty(showUpdatedText: String) {
        showAppToast(this, showUpdatedText)
        startActivity(Intent(this, FacultyActivity::class.java))
    }

}