package com.kapilguru.trainer.ui.earnings.model

import com.google.gson.annotations.SerializedName

data class EarningsDataResponse(
    @SerializedName("data") val earningsDataResponseApi: List<EarningsDataResponseApi>?, @SerializedName("message") val message: String = "", @SerializedName("status") val status: Int = 0
)