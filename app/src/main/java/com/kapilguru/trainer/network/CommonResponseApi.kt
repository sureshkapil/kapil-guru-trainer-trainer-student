package com.kapilguru.trainer.network

import com.google.gson.annotations.SerializedName

data class CommonResponseApi(
    @SerializedName("data")
    val commonResponse: CommonResponse,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)