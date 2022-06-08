package com.kapilguru.trainer.ui.courses.batchesList.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class BatchListApi(
    @SerializedName("data") val batchListResponse:  List<BatchListResponse>,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)