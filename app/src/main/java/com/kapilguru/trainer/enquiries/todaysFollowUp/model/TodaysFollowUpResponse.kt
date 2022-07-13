package com.kapilguru.trainer.enquiries.todaysFollowUp.model

import com.google.gson.annotations.SerializedName

data class TodaysFollowUpResponse(
    @SerializedName("data") val todaysFollowUpList: ArrayList<TodaysFollowUpResData>? = null,
    @SerializedName("message") val message: String = "",
    @SerializedName("status") val status: Int = 0)