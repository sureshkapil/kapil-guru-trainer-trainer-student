package com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data

import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.ui.courses.addcourse.models.AddCourseApiData

data class AddGuestLectureResponse(
    @SerializedName("data") val addGuestLectureResData: AddGuestLectureResData?,
    val message: String,
    val status: Int
)
