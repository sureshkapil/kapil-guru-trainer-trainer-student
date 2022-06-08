package com.kapilguru.trainer.ui.courses.addcourse.models

import com.google.gson.annotations.SerializedName

data class UploadImageCourseResponse(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)