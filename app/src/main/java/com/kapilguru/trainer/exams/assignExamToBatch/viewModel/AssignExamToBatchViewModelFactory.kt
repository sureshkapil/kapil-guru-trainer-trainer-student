package com.kapilguru.trainer.exams.assignExamToBatch.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.exams.assignExamToBatch.AssignExamToBatchRepository

class AssignExamToBatchViewModelFactory(private val apiHelper : ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AssignExamToBatchViewModel::class.java)) {
            return AssignExamToBatchViewModel(
                AssignExamToBatchRepository(apiHelper)
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}