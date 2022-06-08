package com.kapilguru.trainer.allSubscription.mySubscriptions.model

import com.google.gson.annotations.SerializedName

data class MySubscriptionResponse(
    @SerializedName("allData") val mySubscriptionData: MySubscriptionData,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)
