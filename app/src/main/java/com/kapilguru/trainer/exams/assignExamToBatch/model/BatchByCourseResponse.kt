package com.kapilguru.trainer.exams.assignExamToBatch.model

import com.google.gson.annotations.SerializedName

data class BatchByCourseResponse(
    @SerializedName("data") val batchesByCourseList: ArrayList<BatchByCourseResData>?,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)
