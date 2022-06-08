package com.kapilguru.trainer.ui.courses.batchesList.batchStudents.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class BatchStudentListApi(
    @SerializedName("data") val data: List<BatchStudentListResponse>,
    @SerializedName("message")val message: String,
    @SerializedName("status")  val status: Int
)