package com.kapilguru.trainer.student.myClassRoomDetails.model

import com.google.gson.annotations.SerializedName

data class ReviewStudentResponse(
    @SerializedName("status") var status: Int,
    @SerializedName("message") val message: String
)
