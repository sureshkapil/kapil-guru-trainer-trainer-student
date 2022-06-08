package com.kapilguru.trainer.ui.courses.addcourse.models

import com.google.gson.annotations.SerializedName

data class GetCourseResponse(
    @SerializedName("data") val addCourseRequest: List<AddCourseRequest>?,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)