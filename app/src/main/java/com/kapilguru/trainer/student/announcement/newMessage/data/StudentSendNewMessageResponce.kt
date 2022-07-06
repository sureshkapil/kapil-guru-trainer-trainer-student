package com.kapilguru.trainer.student.announcement.newMessage.data

import com.google.gson.annotations.SerializedName

class StudentSendNewMessageResponce(
    @SerializedName("data") val data: StudentSendNewMessageResponceData? = null,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)