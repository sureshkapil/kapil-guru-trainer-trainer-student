package com.kapilguru.trainer.studentExamBatchResult

import com.google.gson.annotations.SerializedName

data class StudentAnswerSheetApi(
    @SerializedName("allData")
    val studentAnswerSheetResponse: StudentAnswerSheetResponse,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)