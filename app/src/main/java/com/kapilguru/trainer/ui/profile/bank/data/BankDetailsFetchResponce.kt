package com.kapilguru.trainer.ui.profile.bank.data

import com.google.gson.annotations.SerializedName

data class BankDetailsFetchResponce(
    @SerializedName("data") val data: List<BankDetailsFetchResData>,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)