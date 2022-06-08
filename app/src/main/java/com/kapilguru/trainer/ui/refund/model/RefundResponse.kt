package com.kapilguru.trainer.ui.refund.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class RefundResponse(
    @SerializedName("data") val refundData: List<RefundData>,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)