package com.kapilguru.trainer.feeManagement.addFeeManagement

import com.google.gson.annotations.SerializedName

data class AddFeeManagementResponse(
    @SerializedName("data") val addFeeDetailsResponseApi: AddFeeManagementResponseApi, @SerializedName("message") val message: String = "", @SerializedName("status") val status: Int = 0
)