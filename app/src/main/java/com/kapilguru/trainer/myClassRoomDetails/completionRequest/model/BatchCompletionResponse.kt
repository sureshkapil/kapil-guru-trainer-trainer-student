package com.kapilguru.trainer.myClassRoomDetails.completionRequest.model

import com.google.gson.annotations.SerializedName

data class BatchCompletionResponse(
    @SerializedName("allData") val batchCompletionResponseData: BatchCompletionResponseData,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
) {
}