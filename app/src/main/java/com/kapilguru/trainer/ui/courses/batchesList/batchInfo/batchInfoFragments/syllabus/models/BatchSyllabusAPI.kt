package com.kapilguru.trainer.ui.courses.batchesList.batchInfo.batchInfoFragments.syllabus.models

import com.google.gson.annotations.SerializedName

data class BatchSyllabusAPI(
    @SerializedName("data") val batchSyllabusResponse: List<BatchSyllabusResponse>,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)