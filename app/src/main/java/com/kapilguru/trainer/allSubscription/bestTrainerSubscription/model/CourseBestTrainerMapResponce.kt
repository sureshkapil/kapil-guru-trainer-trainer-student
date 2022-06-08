package com.kapilguru.trainer.allSubscription.bestTrainerSubscription.model

import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.allSubscription.positionSubscription.model.CoursePositionMapResData

data class CourseBestTrainerMapResponce(
    @SerializedName("data") val courseBestTrainerMapResData : CoursePositionMapResData,
    @SerializedName("message") val message : String,
    @SerializedName("status") val status : Int)