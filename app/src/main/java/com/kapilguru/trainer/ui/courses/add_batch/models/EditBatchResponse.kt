package com.kapilguru.trainer.ui.courses.add_batch.models

import com.google.gson.annotations.SerializedName

data class EditBatchResponse(
    @SerializedName("data") val data: Int? = 0, @SerializedName("message") val message: String? = "", @SerializedName("status") val status: Int? = 0
)