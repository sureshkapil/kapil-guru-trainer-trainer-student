package com.kapilguru.trainer.ui.earnings.history.model

import com.google.gson.annotations.SerializedName

data class EarningsHistoryResponseApi(
    @SerializedName("data")
    val earningsHistoryResponse: List<EarningsHistoryResponse>?,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)