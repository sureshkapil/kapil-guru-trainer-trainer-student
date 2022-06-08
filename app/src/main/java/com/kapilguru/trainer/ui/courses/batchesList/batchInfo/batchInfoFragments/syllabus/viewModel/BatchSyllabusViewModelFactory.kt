package com.kapilguru.trainer.ui.courses.batchesList.batchInfo.batchInfoFragments.syllabus.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.courses.batchesList.batchInfo.batchInfoFragments.syllabus.BatchSyllabusRepository

class BatchSyllabusViewModelFactory (private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BatchSyllabusViewModel::class.java)) {
            return BatchSyllabusViewModel(
                BatchSyllabusRepository(apiHelper)
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}
