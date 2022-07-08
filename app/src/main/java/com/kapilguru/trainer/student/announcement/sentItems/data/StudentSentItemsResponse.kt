package com.kapilguru.trainer.student.announcement.sentItems.data

import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.student.announcement.sentItems.data.StudentSentItemsData

data class StudentSentItemsResponse(
    @SerializedName("data") val data: List<StudentSentItemsData>? = null,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)
