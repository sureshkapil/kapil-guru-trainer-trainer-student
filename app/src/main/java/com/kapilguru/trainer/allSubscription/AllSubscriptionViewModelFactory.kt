package com.kapilguru.trainer.allSubscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper

class AllSubscriptionViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllSubscriptionViewModel::class.java)) {
            return AllSubscriptionViewModel(
                AllSubscriptionRepository(
                    apiHelper
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}