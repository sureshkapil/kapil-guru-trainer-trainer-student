package com.kapilguru.trainer.student.myClassRoomDetails.model

import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.student.myClassRoomDetails.model.StudentBatchDetailsData

data class StudentBatchDetailsResponse(
    @SerializedName("data") var studentBatchDetailsData: List<StudentBatchDetailsData>? = null,
    @SerializedName("message") var message: String? = "",
    @SerializedName("status") var status: Int? = 0
)
