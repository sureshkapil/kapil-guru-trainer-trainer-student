package com.kapilguru.trainer.allSubscription.positionSubscription.model

import com.google.gson.annotations.SerializedName

data class CoursePositionMapResponse(
    @SerializedName("data") val coursePositionMapResData : CoursePositionMapResData,
    @SerializedName("message") val message : String,
    @SerializedName("status") val status : Int)
