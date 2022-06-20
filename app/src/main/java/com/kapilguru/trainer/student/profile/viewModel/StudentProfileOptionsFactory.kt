package com.kapilguru.trainer.student.profile.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.student.profile.StudentProfileOptionsRepository

class StudentProfileOptionsFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentProfileOptionsViewModel::class.java)) {
            return StudentProfileOptionsViewModel(
                StudentProfileOptionsRepository(apiHelper)
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}