package com.kapilguru.trainer.ui.earnings.model

import com.google.gson.annotations.SerializedName

data class EarningsListResponse(
    @SerializedName("data") var earningsListApiData: List<EarningsListApiData>,
    @SerializedName("message") var message: String,
    @SerializedName("status") var status: Int
)