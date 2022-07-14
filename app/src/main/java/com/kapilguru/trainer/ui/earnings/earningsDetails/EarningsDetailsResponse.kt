package com.kapilguru.trainer.ui.earnings.earningsDetails

import com.google.gson.annotations.SerializedName

data class EarningsDetailsResponse(
    @SerializedName("data") val earningsDetailsResponseApi: List<EarningsDetailsResponseApi>?, @SerializedName("message") val message: String = "", @SerializedName("status") val status: Int = 0
)