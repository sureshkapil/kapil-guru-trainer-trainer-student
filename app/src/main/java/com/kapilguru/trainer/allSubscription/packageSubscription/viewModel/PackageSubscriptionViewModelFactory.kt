package com.kapilguru.trainer.allSubscription.packageSubscription.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.allSubscription.packageSubscription.PackageSubscriptionRepository

class PackageSubscriptionViewModelFactory(private val apiHelper: ApiHelper) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PackageSubscriptionViewModel::class.java)) {
            return PackageSubscriptionViewModel(PackageSubscriptionRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}