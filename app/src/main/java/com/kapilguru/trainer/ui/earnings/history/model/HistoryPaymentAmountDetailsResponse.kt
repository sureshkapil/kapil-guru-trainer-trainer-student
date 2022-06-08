package com.kapilguru.trainer.ui.earnings.history.model

import com.google.gson.annotations.SerializedName

data class HistoryPaymentAmountDetailsResponse(
    @SerializedName("summary")
    var summary: Summary,
    @SerializedName("courses")
    var courses: String? = null,
    @SerializedName("referrals")
    var referrals: String? = null,
    @SerializedName("payment")
    var payment: String? = null,
    @SerializedName("webinars")
    var webinars: String? = null
)