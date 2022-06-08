package com.kapilguru.trainer.batchExamReports

import com.google.gson.annotations.SerializedName

data class BatchStudentReportApi(
    @SerializedName("allData")
    val batchStudentReportResponse: BatchStudentReportResponse,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)