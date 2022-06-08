package com.kapilguru.trainer.allSubscription.positionSubscription.model

import com.google.gson.annotations.SerializedName

data class TrainerCoursePositionDetailsRespose(
    @SerializedName("allData") val allData : TrainerCoursePositionDetails,
    @SerializedName("message") val message : String,
    @SerializedName("status") val status : Int)
