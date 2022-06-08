package com.kapilguru.trainer.ui.profile.profileOrganisation

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.profile.data.ProfileData

class ProfileOrgRepository(private val apiHelper: ApiHelper) {
    suspend fun getCountryList() = apiHelper.getCountryList()
    suspend fun getSateList(countryId: Int) = apiHelper.getStateList(countryId)
    suspend fun getCityList(stateId: Int) = apiHelper.getCityList(stateId)
    suspend fun saveProfile(profileData: ProfileData) = apiHelper.saveProfileData(profileData)
    suspend fun updateProfile(userId: String, profileData: ProfileData) = apiHelper.updateProfileData(userId, profileData)
}