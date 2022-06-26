package com.kapilguru.trainer.faculty.addFaculty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.BaseActivity
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityAddFacultyBinding
import com.kapilguru.trainer.databinding.ActivityFacultyBinding
import com.kapilguru.trainer.faculty.FacultyViewModel
import com.kapilguru.trainer.faculty.FacultyViewModelFactory
import com.kapilguru.trainer.network.RetrofitNetwork

class AddFacultyActivity : BaseActivity() {

    lateinit var binding: ActivityAddFacultyBinding
    lateinit var viewModel: FacultyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_faculty)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_faculty)
        viewModel = ViewModelProvider(this, FacultyViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(FacultyViewModel::class.java)
        setCustomActionBarListener()
    }


    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.add_faculty))
    }

}