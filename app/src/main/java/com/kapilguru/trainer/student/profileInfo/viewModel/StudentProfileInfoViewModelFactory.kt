package com.kapilguru.trainer.student.profileInfo.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.student.profileInfo.StudentProfileInfoRepository

class StudentProfileInfoViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentProfileInfoViewmodel::class.java)) {
            return StudentProfileInfoViewmodel(
                StudentProfileInfoRepository(apiHelper)
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}