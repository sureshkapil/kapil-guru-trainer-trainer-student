package com.kapilguru.trainer.ui.otpLogin

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.forgotPassword.model.ValidateMobileRequest
import com.kapilguru.trainer.signup.model.validateMail.ValidateMailRequest
import com.kapilguru.trainer.ui.otpLogin.model.OTPLoginRequest
import com.kapilguru.trainer.ui.otpLogin.model.OTPLoginValidateRequest

class OTPLoginRepository(private val apiHelper: ApiHelper) {
    suspend fun validateMail(validateMailReq : ValidateMailRequest) = apiHelper.validateMail(validateMailReq)
    suspend fun validateMobile(validateMobileRequest: ValidateMobileRequest) = apiHelper.validateMobile(validateMobileRequest)
    suspend fun validateOTPLogin(otpLoginValidateReq : OTPLoginValidateRequest) = apiHelper.validateOTPLogin(otpLoginValidateReq)
    suspend fun requestOTP(otpLoginReq : OTPLoginValidateRequest) = apiHelper.requestOTP(otpLoginReq)
    suspend fun otpLogin(otpLoginRequest : OTPLoginRequest) = apiHelper.otpLogin(otpLoginRequest)
}