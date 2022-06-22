package com.kapilguru.trainer.student.profile

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.profile.data.CheckOTPRequest
import com.kapilguru.trainer.ui.profile.data.GetOTPRequest

class StudentProfileOptionsRepository(private val apiHelper: ApiHelper) {
    suspend fun getStudentProfileData(userId: String) = apiHelper.getStudentProfileData(userId)
    suspend fun getOTRequest(getOTPRequest: GetOTPRequest) = apiHelper.generateOTP(getOTPRequest)
    suspend fun checkOTP(checkOTPRequest: CheckOTPRequest) = apiHelper.checkOTP(checkOTPRequest)
}