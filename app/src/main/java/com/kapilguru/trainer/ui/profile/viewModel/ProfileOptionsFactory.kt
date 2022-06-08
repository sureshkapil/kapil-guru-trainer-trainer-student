package com.kapilguru.trainer.ui.profile.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.profile.ProfileOptionsRepository

class ProfileOptionsFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileOptionsViewModel::class.java)) {
            return ProfileOptionsViewModel(
                ProfileOptionsRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}