package com.kapilguru.trainer.allSubscription.positionSubscription.model

import com.google.gson.annotations.SerializedName

data class CourseOwnedData(
    @SerializedName("id") val id: Int,
    @SerializedName("position_id") val positionId: Int,
    @SerializedName("course_id") val courseId: Int,
    @SerializedName("course_position_num") val coursePositionNum: Int
)
