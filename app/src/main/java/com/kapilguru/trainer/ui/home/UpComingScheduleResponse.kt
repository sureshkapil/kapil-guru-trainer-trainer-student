package com.kapilguru.trainer.ui.home

import com.google.gson.annotations.SerializedName

data class UpComingScheduleResponse(
    @SerializedName("data")
    var data: List<UpComingScheduleApi>?,
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Int = 0
)