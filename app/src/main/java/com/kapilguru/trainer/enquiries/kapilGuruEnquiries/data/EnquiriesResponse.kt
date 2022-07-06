package com.kapilguru.trainer.enquiries.kapilGuruEnquiries.data

import com.google.gson.annotations.SerializedName

data class EnquiriesResponse(
    @SerializedName("data") val enquiries: ArrayList<EnquiriesResData> ? = null,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int)