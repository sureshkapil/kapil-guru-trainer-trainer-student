package com.kapilguru.trainer.ui.courses.courses_list.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CourseApi(
    @SerializedName("data") val courseResponse: List<CourseResponse>,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)