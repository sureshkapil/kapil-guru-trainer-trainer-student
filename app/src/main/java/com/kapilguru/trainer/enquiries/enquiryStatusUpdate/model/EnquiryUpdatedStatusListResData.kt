package com.kapilguru.trainer.enquiries.enquiryStatusUpdate.model

import com.google.gson.annotations.SerializedName

data class EnquiryUpdatedStatusListResData(
    @SerializedName("follow_up_date") val followUpDate: String? = null,
    @SerializedName("comments") val comments: String? = null,
    @SerializedName("is_active") val isActive: Int = 0,
    @SerializedName("modified_by") val modifiedBy: Int? = null,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("created_date") val createdDate: String = "",
    @SerializedName("modified_date") val modifiedDate: String? = null,
    @SerializedName("enquiry_id") val enquiryId: Int = 0,
    @SerializedName("created_by") val createdBy: Int = 0,
    @SerializedName("status") val status: String = ""
)