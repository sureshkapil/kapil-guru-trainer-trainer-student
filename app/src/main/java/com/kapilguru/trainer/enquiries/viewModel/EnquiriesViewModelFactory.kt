package com.kapilguru.trainer.enquiries.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.enquiries.EnquiriesRepository

class EnquiriesViewModelFactory(private val apiHelper: ApiHelper, private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EnquiriesViewModel::class.java)) {
            return EnquiriesViewModel(
                EnquiriesRepository(apiHelper), application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}