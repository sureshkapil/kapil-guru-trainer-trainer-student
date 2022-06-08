package com.kapilguru.trainer.ui.profile.profileInfo.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.profile.profileInfo.ProfileInfoRepository

class ProfileInfoViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileInfoViewmodel::class.java)) {
            return ProfileInfoViewmodel(
                ProfileInfoRepository(apiHelper)
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}