package com.kapilguru.trainer.student.exam

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper

class StudentExamViewModelFactory(private val apiHelper: ApiHelper, private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentExamViewModel::class.java)) {
            return StudentExamViewModel(
                StudentExamRepository(apiHelper), application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}