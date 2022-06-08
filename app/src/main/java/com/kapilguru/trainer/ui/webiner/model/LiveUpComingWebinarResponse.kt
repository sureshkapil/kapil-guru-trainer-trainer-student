package com.kapilguru.trainer.ui.webiner.model

import com.google.gson.annotations.SerializedName

data class LiveUpComingWebinarResponse(
    @SerializedName("data") var webinarData: ArrayList<LiveUpComingWebinarData>,
    @SerializedName("message") var message: String,
    @SerializedName("status") var status: Int)