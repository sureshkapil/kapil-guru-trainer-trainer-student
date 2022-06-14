package com.kapilguru.trainer.signup.model.validateOtp

import com.kapilguru.trainer.BuildConfig

data class ValidateOtpRequest(
    var otp: String? = null,
    var uuid: String? = null,
    var package_name: String? = BuildConfig.PACKAGE_ID,
)