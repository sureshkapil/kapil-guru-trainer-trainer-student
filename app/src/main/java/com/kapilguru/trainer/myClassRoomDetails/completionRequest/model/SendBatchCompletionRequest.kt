package com.kapilguru.trainer.myClassRoomDetails.completionRequest.model

import com.google.gson.annotations.SerializedName

data class SendBatchCompletionRequest(
    @SerializedName("trainer_id") var trainerId: String = "",
    @SerializedName("course_id") var courseId: Int = -1,
    @SerializedName("batch_id") var batchId: Int = -1,
    @SerializedName("request_date") var requestDate: String = "",
    @SerializedName("status") var status: String = "Requested"
)