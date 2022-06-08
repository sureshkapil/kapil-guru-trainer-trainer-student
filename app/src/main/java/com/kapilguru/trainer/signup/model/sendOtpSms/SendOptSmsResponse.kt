package com.kapilguru.trainer.signup.model.sendOtpSms

import com.google.gson.annotations.SerializedName

data class SendOptSmsResponse(
    @SerializedName("data") var sendOptSmsResponseApi: List<SendOptSmsResponseApi>?,
    @SerializedName("message") var message: String?,
    @SerializedName("status") var status: Int?,
)