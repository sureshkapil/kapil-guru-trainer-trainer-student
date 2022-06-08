package com.kapilguru.trainer.ui.courses.addcourse.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UpdateCourseApi(
    @SerializedName("data")
    val updateCourseResponse: UpdateCourseResponse? = null,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)