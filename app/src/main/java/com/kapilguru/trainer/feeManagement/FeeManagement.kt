package com.kapilguru.trainer.feeManagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.BaseActivity
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityFacultyBinding
import com.kapilguru.trainer.databinding.ActivityFeeManagementBinding
import com.kapilguru.trainer.faculty.FacultyViewModel
import com.kapilguru.trainer.faculty.FacultyViewModelFactory
import com.kapilguru.trainer.faculty.addFaculty.AddFacultyActivity
import com.kapilguru.trainer.network.RetrofitNetwork

class FeeManagement : BaseActivity() {

    lateinit var binding: ActivityFeeManagementBinding
    lateinit var viewModel: FeeManagementViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_fee_management)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fee_management)
        viewModel = ViewModelProvider(this, FeeManagementViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(FeeManagementViewModel::class.java)
        binding.viewModel = viewModel
        setCustomActionBarListener()
        setClickListeners()
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.fee_management))
    }

    private fun setClickListeners() {
        binding.buttonAdd.setOnClickListener {
            navigateToAddFeeManagement()
        }
    }

    private fun navigateToAddFeeManagement() {
        startActivity(Intent(this, AddFeeManagement::class.java))
    }

}