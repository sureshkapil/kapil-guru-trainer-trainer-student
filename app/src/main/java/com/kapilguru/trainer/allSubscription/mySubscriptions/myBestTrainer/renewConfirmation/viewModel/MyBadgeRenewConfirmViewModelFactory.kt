package com.kapilguru.trainer.allSubscription.mySubscriptions.myBestTrainer.renewConfirmation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.allSubscription.mySubscriptions.myBestTrainer.renewConfirmation.MyBadgeRenewConfirmRepository

class MyBadgeRenewConfirmViewModelFactory(private val apiHelper : ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyBadgeRenewConfirmViewModel::class.java)) {
            return MyBadgeRenewConfirmViewModel(MyBadgeRenewConfirmRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}