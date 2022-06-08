package com.kapilguru.trainer.ui.profile.profileOrganisation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.login.LoginRepository
import com.kapilguru.trainer.ui.profile.profileOrganisation.ProfileOrgRepository

class ProfileOrgViewModelFactory(private val apiHelper : ApiHelper) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileOrgViewModel::class.java)) {
            return ProfileOrgViewModel(
                ProfileOrgRepository(apiHelper)
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}