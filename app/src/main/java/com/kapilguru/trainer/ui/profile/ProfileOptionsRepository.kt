package com.kapilguru.trainer.ui.profile

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.login.models.LoginUserRequest
import com.kapilguru.trainer.ui.profile.data.CheckOTPRequest
import com.kapilguru.trainer.ui.profile.data.GetOTPRequest
import com.kapilguru.trainer.ui.profile.data.ProfileData

class ProfileOptionsRepository(private val apiHelper: ApiHelper) {
    suspend fun getProfileData(userId: String) = apiHelper.getProfileData(userId)
    suspend fun getOTRequest(getOTPRequest : GetOTPRequest) = apiHelper.generateOTP(getOTPRequest)
    suspend fun checkOTP(checkOTPRequest: CheckOTPRequest) = apiHelper.checkOTP(checkOTPRequest)
}