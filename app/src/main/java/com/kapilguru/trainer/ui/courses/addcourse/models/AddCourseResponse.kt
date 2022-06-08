package com.kapilguru.trainer.ui.courses.addcourse.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AddCourseResponse(
        @SerializedName("data") val addCourseApiData: AddCourseApiData?,
        @SerializedName("message") val message: String,
        @SerializedName("status") val status: Int
)