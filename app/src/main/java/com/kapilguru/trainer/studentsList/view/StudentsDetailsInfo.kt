package com.kapilguru.trainer.studentsList.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityStudentsDeatilsInfoBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.studentsList.model.StudentDetails
import com.kapilguru.trainer.studentsList.viewModel.StudentListViewModel
import com.kapilguru.trainer.studentsList.viewModel.StudentListViewModelFactory

class StudentsDetailsInfo : AppCompatActivity() {

    lateinit var viewBinding: ActivityStudentsDeatilsInfoBinding
    lateinit var viewModelBinding: StudentListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        viewBinding = DataBindingUtil.setContentView(this,R.layout.activity_students_deatils_info)

        viewModelBinding = ViewModelProvider(this,
            StudentListViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))
        ).get(StudentListViewModel::class.java)


        viewBinding.vieModel = viewModelBinding


       val data = intent.getParcelableExtra<StudentDetails>("StudentsList")

        data?.let {
            viewModelBinding.studentDetails.value = it
        }

    }

}