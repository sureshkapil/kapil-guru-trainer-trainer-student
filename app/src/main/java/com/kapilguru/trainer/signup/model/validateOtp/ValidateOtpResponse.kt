package com.kapilguru.trainer.signup.model.validateOtp

import com.google.gson.annotations.SerializedName

data class ValidateOtpResponse(
    @SerializedName("data")val validateOtpResData: ValidateOtpResData,
    val message: String,
    val status: Int
)