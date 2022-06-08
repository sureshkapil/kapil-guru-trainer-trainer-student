package com.kapilguru.trainer.myClassRoomDetails.completionRequest.model

import com.google.gson.annotations.SerializedName

data class BatchCompletionResSummary(
    @SerializedName("trainer_id") var trainerId: Int = -1,
    @SerializedName("course_id") var courseId: Int = -1,
    @SerializedName("batch_id") var batchId: Int = -1
)
