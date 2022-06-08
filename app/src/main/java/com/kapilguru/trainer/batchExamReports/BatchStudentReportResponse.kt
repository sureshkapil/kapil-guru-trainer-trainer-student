package com.kapilguru.trainer.batchExamReports

import com.google.gson.annotations.SerializedName

data class BatchStudentReportResponse(
    @SerializedName("summary")
    val summary: String = "",
    @SerializedName("exam_students")
    val examStudents: String = "",
    @SerializedName("exam_results")
    val examResults: String = ""
)