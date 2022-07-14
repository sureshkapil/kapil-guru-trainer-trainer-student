package com.kapilguru.trainer.feeManagement.feeFollowUps

import com.google.gson.annotations.SerializedName

data class FeeFollowUpResponse(
    @SerializedName("data") val feeFollowUpResponseApi: List<FeeFollowUpResponseApi>?, @SerializedName("message") val message: String = "", @SerializedName("status") val status: Int = 0
)