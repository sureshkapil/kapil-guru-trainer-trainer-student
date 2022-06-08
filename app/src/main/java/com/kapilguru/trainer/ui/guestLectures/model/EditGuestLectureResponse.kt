package com.kapilguru.trainer.ui.guestLectures.model

import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.ui.courses.addcourse.models.AddCourseApiData

data class EditGuestLectureResponse(
    @SerializedName("status") val status : Int,
    @SerializedName("message") val message : String,
    @SerializedName("data") val data : AddCourseApiData
)
