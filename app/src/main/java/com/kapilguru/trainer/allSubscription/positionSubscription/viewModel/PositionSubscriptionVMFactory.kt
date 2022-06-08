package com.kapilguru.trainer.allSubscription.positionSubscription.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.MyApplication
import com.kapilguru.trainer.allSubscription.positionSubscription.PositionSubscriptionRepository

class PositionSubscriptionVMFactory(private val apiHelper: ApiHelper,private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PositionSubscriptionViewModel::class.java)) {
            return PositionSubscriptionViewModel(PositionSubscriptionRepository(apiHelper),application) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}