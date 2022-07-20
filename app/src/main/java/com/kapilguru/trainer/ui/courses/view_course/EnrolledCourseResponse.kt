package com.kapilguru.trainer.ui.courses.view_course

import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.ui.courses.view_course.EnrolledCourseResponseApi

data class EnrolledCourseResponse(
    @SerializedName("data")
    var data: List<EnrolledCourseResponseApi>?=null,
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("status")
    var status: Int? = 0
)