package com.kapilguru.trainer.ui.otpLogin.model

import com.google.gson.annotations.SerializedName

data class OTPLoginValidateResData(
    @SerializedName("result") var result: Int? = -1,
)
