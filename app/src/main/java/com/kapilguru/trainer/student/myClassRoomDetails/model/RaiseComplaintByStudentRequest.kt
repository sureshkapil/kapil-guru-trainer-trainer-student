package com.kapilguru.trainer.student.myClassRoomDetails.model

import com.google.gson.annotations.SerializedName

data class RaiseComplaintByStudentRequest(

    @SerializedName("user_id") val userId: String? = null,
    @SerializedName("on_whom") var onWhom: Int,
    @SerializedName("text") val reason: String? = null
)
