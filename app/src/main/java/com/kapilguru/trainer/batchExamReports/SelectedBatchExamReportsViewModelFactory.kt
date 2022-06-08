package com.kapilguru.trainer.batchExamReports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper

class SelectedBatchExamReportsViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SelectedBatchExamReportsViewModel::class.java)) {
            return SelectedBatchExamReportsViewModel(
                SelectedBatchExamReportsRepository(apiHelper)
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}