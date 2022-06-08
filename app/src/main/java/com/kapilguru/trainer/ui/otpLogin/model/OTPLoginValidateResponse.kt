package com.kapilguru.trainer.ui.otpLogin.model

import com.google.gson.annotations.SerializedName

data class OTPLoginValidateResponse(
    @SerializedName("data") val otpLoginValidateResData : List<OTPLoginValidateResData>? = null,
    @SerializedName("message") val message : String ? = "",
    @SerializedName("status") val status : Int? = -1
)
