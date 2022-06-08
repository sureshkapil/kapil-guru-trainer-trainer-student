package com.kapilguru.trainer.studentExamBatchResult

import com.google.gson.annotations.SerializedName

data class BatchExamStudentResultResponse(
    @SerializedName("summary")
    val summary: String = "",
    @SerializedName("student_results")
    val studentResults: String = ""
)