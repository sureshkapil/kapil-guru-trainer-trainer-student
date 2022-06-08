package com.kapilguru.trainer.myClassRoomDetails.completionRequest.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.BatchCompletionReqRepository

class BatchCompletionReqFactory(private val apiHelper: ApiHelper, val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BatchCompletionReqViewModel::class.java)) {
            return BatchCompletionReqViewModel(
                BatchCompletionReqRepository(apiHelper), application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}