package com.kapilguru.trainer.ui.courses.add_batch.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.courses.add_batch.AddBatchRepository


class AddBatchViewModelFactory(private val apiHelper: ApiHelper,
  private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddBatchViewModel::class.java)) {
            return AddBatchViewModel(
                    AddBatchRepository(apiHelper),application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}