package com.kapilguru.trainer.allSubscription.positionSubscription.confirmation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.allSubscription.positionSubscription.confirmation.PositionSubConfirmRepository
import com.kapilguru.trainer.login.LoginRepository
import com.kapilguru.trainer.login.viewModel.LoginViewModel

class PositionSubConfirmViewModelFactory(private val apiHelper : ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PositionSubConfirmViewModel::class.java)) {
            return PositionSubConfirmViewModel(
                PositionSubConfirmRepository(apiHelper)
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}