package com.kapilguru.trainer.addStudent

import com.google.gson.annotations.SerializedName

data class OfflineStudentsListResponse(
    @SerializedName("data") val data: List<OfflineStudentsListResponseApi>?, @SerializedName("message") val message: String = "", @SerializedName("status") val status: Int = 0
)