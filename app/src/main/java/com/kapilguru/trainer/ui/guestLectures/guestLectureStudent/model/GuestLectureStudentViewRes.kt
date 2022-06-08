package com.kapilguru.trainer.ui.guestLectures.guestLectureStudent.model


import com.google.gson.annotations.SerializedName

class GuestLectureStudentViewRes(
    @SerializedName("data") val demoLectureDataList: ArrayList<GuestLectureStudentViewResData>? = null,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)