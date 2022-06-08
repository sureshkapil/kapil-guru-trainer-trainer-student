package com.kapilguru.trainer.studentExamBatchResult

import com.google.gson.annotations.SerializedName

data class BatchExamStudentResultApi(
    @SerializedName("allData")
    val batchExamStudentResultResponse: BatchExamStudentResultResponse,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)