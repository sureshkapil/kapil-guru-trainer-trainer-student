package com.kapilguru.trainer.feeManagement.paidRecords

import com.google.gson.annotations.SerializedName

data class StudentFeePaidResponse(
    @SerializedName("data") val studentFeeRecordResponseApi: List<StudentFeePaidResponseApi>?, @SerializedName("message") val message: String = "", @SerializedName("status") val status: Int = 0
)