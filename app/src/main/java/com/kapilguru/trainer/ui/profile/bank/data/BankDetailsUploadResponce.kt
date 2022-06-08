package com.kapilguru.trainer.ui.profile.bank.data

import com.google.gson.annotations.SerializedName

data class BankDetailsUploadResponce(
    @SerializedName("data") val bankDetailsUploadResData: BankDetailsUploadResData,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)