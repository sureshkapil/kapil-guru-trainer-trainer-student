package com.kapilguru.trainer.ui.courses.batchesList.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.courses.batchesList.BatchListRepository

class BatchListViewModelFactory(
    private val apiHelper: ApiHelper,
    private val appliaction: Application,
    private val courseid: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BatchListViewModel::class.java)) {
            return BatchListViewModel(
                    BatchListRepository(apiHelper),
                appliaction,courseid) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}