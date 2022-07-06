package com.kapilguru.trainer.student.announcement.newMessage.data

import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.student.announcement.newMessage.data.StudentNewMessageData

data class StudentNewMessageResponse(
    @SerializedName("data") val data: List<StudentNewMessageData> ? = null,
    @SerializedName("message") val message: String? = "",
    @SerializedName("status") val status: Int? = 0)
