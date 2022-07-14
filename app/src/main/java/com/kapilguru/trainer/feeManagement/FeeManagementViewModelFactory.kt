package com.kapilguru.trainer.feeManagement

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper

class FeeManagementViewModelFactory(private val apiHelper: ApiHelper, val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeeManagementViewModel::class.java)) {
            return FeeManagementViewModel(
                FeeManagementRepository(apiHelper),application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}