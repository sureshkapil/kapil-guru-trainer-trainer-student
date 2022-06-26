package com.kapilguru.trainer.faculty

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.BaseActivity
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityFacultyBinding
import com.kapilguru.trainer.faculty.addFaculty.AddFacultyActivity
import com.kapilguru.trainer.login.viewModel.LoginViewModel
import com.kapilguru.trainer.login.viewModel.LoginViewModelFactory
import com.kapilguru.trainer.network.RetrofitNetwork

class FacultyActivity : BaseActivity() {

    lateinit var binding: ActivityFacultyBinding
    lateinit var viewModel: FacultyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_faculty)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_faculty)
        viewModel = ViewModelProvider(this, FacultyViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(FacultyViewModel::class.java)
        binding.viewModel = viewModel
        setCustomActionBarListener()
        setClickListeners()
    }


    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.faculty))
    }


    private fun setClickListeners() {
        binding.buttonAddFaculty.setOnClickListener {
            navigateToAddFaculty()
        }
    }

    private fun navigateToAddFaculty() {
        startActivity(Intent(this,AddFacultyActivity::class.java))
    }

}