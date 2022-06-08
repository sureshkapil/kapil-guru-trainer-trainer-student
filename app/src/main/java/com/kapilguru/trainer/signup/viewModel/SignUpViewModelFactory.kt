package com.kapilguru.trainer.signup.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.signup.SignUpRepository

class SignUpViewModelFactory(private val apiHelper : ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(SignUpRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}