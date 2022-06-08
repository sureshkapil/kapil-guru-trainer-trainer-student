package com.kapilguru.trainer.ui.courses.onGoingBatches.models

import com.google.gson.annotations.SerializedName

data class OnGoingBatchApi(
        @SerializedName("allData") val allData: OnGoingBatchesResponse,
        @SerializedName("data") val data: List<Any>,
        @SerializedName("message") val message: String,
        @SerializedName("status") val status: Int
)