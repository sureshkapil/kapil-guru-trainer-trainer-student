package com.kapilguru.trainer.allSubscription.mySubscriptions.model

import com.google.gson.annotations.SerializedName

data class MySubscriptionData(
    @SerializedName("badges") val badges : String?,
    @SerializedName("packages") val packages : String?,
    @SerializedName("positions") val positions : String?
    )
