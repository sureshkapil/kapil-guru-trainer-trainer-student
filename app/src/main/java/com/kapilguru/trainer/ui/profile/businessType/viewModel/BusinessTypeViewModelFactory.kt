package com.kapilguru.trainer.ui.profile.businessType.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BusinessTypeViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BusinessTypeViewModel::class.java)) {
            return BusinessTypeViewModel() as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}