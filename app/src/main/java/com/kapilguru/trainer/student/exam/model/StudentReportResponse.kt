package com.kapilguru.trainer.student.exam.model


import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.student.exam.model.StudentReportApi

data class StudentReportResponse(
    @SerializedName("allData") var studentReportApi: StudentReportApi?,
    @SerializedName("message") var message: String?,
    @SerializedName("status") var status: Int?
)