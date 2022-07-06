package com.kapilguru.trainer.student.announcement.inbox.data

import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.student.announcement.inbox.data.StudentInboxItem

class StudentInboxResponse(
    @SerializedName("data") val data: List<StudentInboxItem> ? = null,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int)