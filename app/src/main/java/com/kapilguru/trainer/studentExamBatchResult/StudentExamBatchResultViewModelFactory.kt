package com.kapilguru.trainer.studentExamBatchResult

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper

class StudentExamBatchResultViewModelFactory(private val apiHelper: ApiHelper, var application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentExamBatchResultViewModel::class.java)) {
            return StudentExamBatchResultViewModel(
                StudentExamBatchResultRepository(apiHelper), application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}