package com.kapilguru.trainer.allSubscription.models

import com.google.gson.annotations.SerializedName

data class AllSubscriptionsResponse(
    @SerializedName("data") val allSubscriptionsData: List<AllSubscriptionsData>,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)