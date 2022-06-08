package com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data

import com.google.gson.annotations.SerializedName

data class AddGuestLectureImageResponse(
    @SerializedName("message") val message: String = "",
    @SerializedName("status") val status: Int = 0
)