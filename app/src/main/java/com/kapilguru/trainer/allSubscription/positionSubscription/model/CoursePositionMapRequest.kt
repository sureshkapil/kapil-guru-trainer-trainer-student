package com.kapilguru.trainer.allSubscription.positionSubscription.model

import com.google.gson.annotations.SerializedName

data class CoursePositionMapRequest(
    @SerializedName("course_id") var courseId : Int,
    @SerializedName("course_position_id") var coursePositionId : Int,
    @SerializedName("expiry_date") var expiryDate : String,
    @SerializedName("start_date") var startDate : String,
    @SerializedName("trainer_id") var trainerId : Int)
