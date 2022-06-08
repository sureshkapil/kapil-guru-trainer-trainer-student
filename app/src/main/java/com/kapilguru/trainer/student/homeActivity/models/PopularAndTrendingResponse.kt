package com.kapilguru.trainer.student.homeActivity.models

import com.google.gson.annotations.SerializedName

data class PopularAndTrendingResponse(
    @SerializedName("data")
    val popularAndTrendingApi: List<PopularAndTrendingApi>?,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)