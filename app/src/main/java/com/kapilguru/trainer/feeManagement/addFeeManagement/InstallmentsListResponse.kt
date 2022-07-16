package com.kapilguru.trainer.feeManagement.addFeeManagement

import com.google.gson.annotations.SerializedName

data class InstallmentsListResponse(
    @SerializedName("data") val installmentsListResponseApi: List<InstallmentsListResponseApi>?, @SerializedName("message") val message: String? = "", @SerializedName("status") val status: Int? = 0
)