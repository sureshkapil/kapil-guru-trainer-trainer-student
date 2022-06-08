package com.kapilguru.trainer.signup

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.forgotPassword.model.ValidateMobileRequest
import com.kapilguru.trainer.signup.model.register.RegisterRequest
import com.kapilguru.trainer.signup.model.sendOtpSms.SendOptSmsResponse
import com.kapilguru.trainer.signup.model.sendOtpSms.SendOtpSmsRequest
import com.kapilguru.trainer.signup.model.validateMail.ValidateMailRequest
import com.kapilguru.trainer.signup.model.validateOtp.ValidateOtpRequest

class SignUpRepository(private val apiHelper : ApiHelper) {
    suspend fun fetchCountryListForSignUp() = apiHelper.fetchCountryListForSignUp()
    suspend fun validateMail(validateMailReq : ValidateMailRequest) = apiHelper.validateMail(validateMailReq)
    suspend fun validateMobile(validateMobileRequest: ValidateMobileRequest) = apiHelper.validateMobile(validateMobileRequest)
    suspend fun validateOtp(validateotpReq : ValidateOtpRequest) = apiHelper.validateOtp(validateotpReq)
    suspend fun registerAccount(registerRequest : RegisterRequest) = apiHelper.registerAccount(registerRequest)
    suspend fun sendOtpMessage(sendOtpSmsRequest: SendOtpSmsRequest)= apiHelper.sendOtpMessage(sendOtpSmsRequest)
}