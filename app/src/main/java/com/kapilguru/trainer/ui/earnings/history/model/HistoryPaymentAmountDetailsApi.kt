package com.kapilguru.trainer.ui.earnings.history.model

import com.google.gson.annotations.SerializedName

data class HistoryPaymentAmountDetailsApi(
    @SerializedName("allData")
    val historyPaymentAmountDetailsResponse: HistoryPaymentAmountDetailsResponse,
    @SerializedName("data")
    val data: List<Object>?,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)