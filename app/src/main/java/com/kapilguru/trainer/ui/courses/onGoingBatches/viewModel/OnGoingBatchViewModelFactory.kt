package com.kapilguru.trainer.ui.courses.onGoingBatches.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.courses.onGoingBatches.OnGoingBatchRepository

class OnGoingBatchViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OnGoingBatchViewModel::class.java)) {
            return OnGoingBatchViewModel(
                OnGoingBatchRepository  (apiHelper)
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}