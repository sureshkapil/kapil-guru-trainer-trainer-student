package com.kapilguru.trainer.ui.webiner.addWebinar.model

import com.google.gson.annotations.SerializedName

data class AddWebinarDetailsResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val addWebinarDetailsResData: AddWebinarDetailsResData
)
