package com.kapilguru.trainer.forgotPassword

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.forgotPassword.model.ChangePasswordRequest
import com.kapilguru.trainer.forgotPassword.model.ValidateMobileRequest
import com.kapilguru.trainer.ui.profile.data.CheckOTPRequest
import com.kapilguru.trainer.ui.profile.data.GetOTPRequest

class ForgotPasswordRepository(private val apiHelper: ApiHelper) {
    suspend fun validateMobile(validateMobileRequest: ValidateMobileRequest) = apiHelper.validateMobile(validateMobileRequest)
    suspend fun getOTP(otpRequest : GetOTPRequest) = apiHelper.generateOTP(otpRequest)
    suspend fun checkOTP(checkOTPReq : CheckOTPRequest) = apiHelper.checkOTP(checkOTPReq)
    suspend fun changePassword(changePasswordReq : ChangePasswordRequest) = apiHelper.changePassword(changePasswordReq)

}