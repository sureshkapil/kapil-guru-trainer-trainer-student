package com.kapilguru.trainer.ui.courses.add_batch.models

import com.google.gson.annotations.SerializedName

data class EditBatchApiRequest(
    @SerializedName("data") val editBatchList: EditBatchList,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)