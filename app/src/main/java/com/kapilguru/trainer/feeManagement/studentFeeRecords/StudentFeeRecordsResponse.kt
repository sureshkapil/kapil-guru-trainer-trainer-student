package com.kapilguru.trainer.feeManagement.studentFeeRecords

import com.google.gson.annotations.SerializedName

data class StudentFeeRecordsResponse(
    @SerializedName("data") val studentFeeRecordsResponseApi: List<StudentFeeRecordsResponseApi>?, @SerializedName("message") val message: String = "", @SerializedName("status") val status: Int = 0
)