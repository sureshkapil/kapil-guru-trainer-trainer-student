package com.kapilguru.trainer.student.homeActivity.liveCourses.model

import com.google.gson.annotations.SerializedName

data class LiveCourseResponse(
    @SerializedName("data") val liveCourseList: ArrayList<LiveCourseResData>?,
    @SerializedName("message") val message: String = "",
    @SerializedName("status") val status: Int = 0
)
