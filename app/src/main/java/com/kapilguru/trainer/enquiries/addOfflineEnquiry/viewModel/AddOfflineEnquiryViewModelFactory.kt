package com.kapilguru.trainer.enquiries.addOfflineEnquiry.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.enquiries.EnquiriesRepository

class AddOfflineEnquiryViewModelFactory(private val apiHelper: ApiHelper, private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddOfflineEnquiryViewModel::class.java)) {
            return AddOfflineEnquiryViewModel(
                EnquiriesRepository(apiHelper), application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}