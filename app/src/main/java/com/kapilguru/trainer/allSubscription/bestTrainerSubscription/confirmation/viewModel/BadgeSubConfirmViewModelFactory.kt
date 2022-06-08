package com.kapilguru.trainer.allSubscription.bestTrainerSubscription.confirmation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.allSubscription.bestTrainerSubscription.confirmation.BadgeSubConfirmRepository

class BadgeSubConfirmViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BadgeSubConfirmViewModel::class.java)) {
            return BadgeSubConfirmViewModel(
                BadgeSubConfirmRepository(apiHelper)
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}