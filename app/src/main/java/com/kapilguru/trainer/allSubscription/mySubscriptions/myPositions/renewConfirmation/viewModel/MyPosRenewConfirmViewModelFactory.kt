package com.kapilguru.trainer.allSubscription.mySubscriptions.myPositions.renewConfirmation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.allSubscription.mySubscriptions.myPositions.renewConfirmation.MyPosRenewConfirmRepository

class MyPosRenewConfirmViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyPosRenewConfirmViewModel::class.java)) {
            return MyPosRenewConfirmViewModel(MyPosRenewConfirmRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}