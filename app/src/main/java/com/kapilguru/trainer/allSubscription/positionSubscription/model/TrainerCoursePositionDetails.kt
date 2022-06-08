package com.kapilguru.trainer.allSubscription.positionSubscription.model

import com.google.gson.annotations.SerializedName

data class TrainerCoursePositionDetails(
    @SerializedName("courses") val courses: String,
    @SerializedName("owned") val owned: String,
    @SerializedName("occupied") val occupied: String
)

