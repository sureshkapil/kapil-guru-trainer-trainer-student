package com.kapilguru.trainer.enquiries.addOfflineEnquiry.data

import com.google.gson.annotations.SerializedName

data class AddEnquiryRes(
    @SerializedName("data") val data: AddEnquiryResData,
    @SerializedName("message") val message: String = "",
    @SerializedName("status") val status: Int = 0
)