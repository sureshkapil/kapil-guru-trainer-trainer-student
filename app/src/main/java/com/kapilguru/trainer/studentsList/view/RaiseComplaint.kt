package com.kapilguru.trainer.studentsList.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityRaiseComplaintBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.studentsList.model.RequestRaiseComplaint
import com.kapilguru.trainer.studentsList.viewModel.StudentListViewModel
import com.kapilguru.trainer.studentsList.viewModel.StudentListViewModelFactory

class RaiseComplaint : BaseActivity() {
    lateinit var viewModel: StudentListViewModel
    lateinit var activityRaiseComplaintBinding: ActivityRaiseComplaintBinding
    lateinit var dialog: CustomProgressDialog
    var studentId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRaiseComplaintBinding = DataBindingUtil.setContentView(this, R.layout.activity_raise_complaint)
        this.setActionbarBackListener(this, activityRaiseComplaintBinding.actionbar, "Raise Complaint")
        viewModel = ViewModelProvider(this, StudentListViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(StudentListViewModel::class.java)
        intent?.let {
            studentId = it.getIntExtra(COMPLAINT_STUDENT_ID, 0)
        }
        activityRaiseComplaintBinding.viewModel = viewModel
        activityRaiseComplaintBinding.clickListeners = this
        activityRaiseComplaintBinding.lifecycleOwner = this
        dialog = CustomProgressDialog(this)
        viewModelObservables()
    }

    private fun viewModelObservables() {
        viewModel.raiseComplaintResult.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    finish()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    fun onSubmit(view: View) {
        if (viewModel.raiseComplaintText.value.toString().trim().isNullOrEmpty()) {
            showAppToast(this, " You Fool this Scenario is tested. please increase your testing knowledge")
        } else {
            val trainerId = StorePreferences(this).trainerId
            val request = RequestRaiseComplaint().also {
                it.onWhom = studentId
                it.text = viewModel.raiseComplaintText.value.toString()
                it.userId = trainerId
            }
            viewModel.raiseComplaintRequest(request)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}