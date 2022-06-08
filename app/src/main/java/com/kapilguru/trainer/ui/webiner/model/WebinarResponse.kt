package com.kapilguru.trainer.ui.webiner.model

import com.google.gson.annotations.SerializedName

data class WebinarResponse(
    @SerializedName("data") var webinarData: ArrayList<ActiveWebinarData>,
    @SerializedName("message") var message: String,
    @SerializedName("status") var status: Int
)