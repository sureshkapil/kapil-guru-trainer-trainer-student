package com.kapilguru.trainer.enquiries.enquiryStatusUpdate.model

import com.google.gson.annotations.SerializedName

data class EnquiryUpdatedStatusListResponse(
    @SerializedName("data") val enquiryUpdatedStatusList: ArrayList<EnquiryUpdatedStatusListResData>? = null,
    @SerializedName("message") val message: String = "",
    @SerializedName("status") val status: Int = 0
)
