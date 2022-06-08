package com.kapilguru.trainer.ui.reports.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.reports.ReportsRepository

class ReportsViewModelFactory(private val apiHelper: ApiHelper,private val application : Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReportsViewModel::class.java)) {
            return ReportsViewModel(
                ReportsRepository(apiHelper),application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}