package com.kapilguru.trainer.allSubscription.bestTrainerSubscription.model

import com.google.gson.annotations.SerializedName

data class BestTrainerResponse(
    @SerializedName("allData") val bestTrainerData : BestTrainerAllData,
    @SerializedName("message") val message : String,
    @SerializedName("status") val status : Int)
