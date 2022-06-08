package com.kapilguru.trainer.ui.earnings.model

import com.google.gson.annotations.SerializedName

data class EarningsDetailsApiResponse(
    @SerializedName("allData") var apiAllData: EarningsDetailsApiAllData,
    @SerializedName("data") var data: List<Any>,
    @SerializedName("message") var message: String,
    @SerializedName("status") var status: Int
)