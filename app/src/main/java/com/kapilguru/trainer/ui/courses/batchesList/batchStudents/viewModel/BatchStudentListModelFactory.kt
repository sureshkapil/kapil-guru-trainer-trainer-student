package com.kapilguru.trainer.ui.courses.batchesList.batchStudents.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.courses.batchesList.batchStudents.BatchStudentsListRepository


class BatchStudentListModelFactory (private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BatchStudentListViewModel::class.java)) {
            return BatchStudentListViewModel(
                BatchStudentsListRepository(apiHelper)
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}