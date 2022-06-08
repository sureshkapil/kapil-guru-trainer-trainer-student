package com.kapilguru.trainer.ui.changePassword.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.changePassword.ChangePasswordRepository

class ChangePasswordViewModelFactory(
    private val apiHelper: ApiHelper,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChangePasswordViewModel::class.java)) {
            return ChangePasswordViewModel(
                ChangePasswordRepository(apiHelper), application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}