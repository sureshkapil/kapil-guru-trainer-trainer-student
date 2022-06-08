package com.kapilguru.trainer.student.homeActivity.models

import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.student.homeActivity.models.AllDemosApi

data class AllDemosResponse(
    @SerializedName("data") val data: List<AllDemosApi>?,
    @SerializedName("message") val message: String = "",
    @SerializedName("status") val status: Int = 0
)