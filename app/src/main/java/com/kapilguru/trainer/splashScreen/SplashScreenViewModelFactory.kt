package com.kapilguru.trainer.splashScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.profile.ProfileOptionsRepository

class SplashScreenViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashScreenViewModel::class.java)) {
            return SplashScreenViewModel(
                ProfileOptionsRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}