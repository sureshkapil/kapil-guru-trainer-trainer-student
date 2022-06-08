package com.kapilguru.trainer.ui.otpLogin.model

import com.google.gson.annotations.SerializedName

data class OTPLoginRequest(
    @SerializedName("otp_value") var otpValue: String = "",
    @SerializedName("uuid") var uuid: String = "")