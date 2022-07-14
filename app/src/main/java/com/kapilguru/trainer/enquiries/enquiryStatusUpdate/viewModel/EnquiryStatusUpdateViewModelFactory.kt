package com.kapilguru.trainer.enquiries.enquiryStatusUpdate.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.enquiries.EnquiriesRepository

class EnquiryStatusUpdateViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EnquiryStatusUpdateViewModel::class.java)) {
            return EnquiryStatusUpdateViewModel(
                EnquiriesRepository(apiHelper)
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}