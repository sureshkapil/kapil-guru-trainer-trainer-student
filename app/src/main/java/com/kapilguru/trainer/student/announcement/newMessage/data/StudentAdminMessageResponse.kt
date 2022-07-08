package com.kapilguru.trainer.student.announcement.newMessage.data

import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.student.announcement.newMessage.data.StudentAdminMessageData

data class StudentAdminMessageResponse(
    @SerializedName("data") val data: List<StudentAdminMessageData> ? = null,
    @SerializedName("message") val message: String? = "",
    @SerializedName("status") val status: Int? = 0)
