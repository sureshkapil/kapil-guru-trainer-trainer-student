package com.kapilguru.trainer.ui.otpLogin.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.otpLogin.OTPLoginRepository

class OTPLoginViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OTPLoginViewModel::class.java)) {
            return OTPLoginViewModel(OTPLoginRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}