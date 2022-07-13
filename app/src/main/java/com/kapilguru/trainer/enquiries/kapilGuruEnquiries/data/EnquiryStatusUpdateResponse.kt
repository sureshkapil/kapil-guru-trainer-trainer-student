package com.kapilguru.trainer.enquiries.kapilGuruEnquiries.data

import com.google.gson.annotations.SerializedName

data class EnquiryStatusUpdateResponse(
    @SerializedName("data") val data: EnquiryStatusUpdateResponseData,
    @SerializedName("message") val message: String = "",
    @SerializedName("status") val status: Int = 0)