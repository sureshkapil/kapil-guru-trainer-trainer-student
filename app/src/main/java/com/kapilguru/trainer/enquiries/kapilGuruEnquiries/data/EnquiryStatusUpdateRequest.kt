package com.kapilguru.trainer.enquiries.kapilGuruEnquiries.data

import com.google.gson.annotations.SerializedName

data class EnquiryStatusUpdateRequest(
    @SerializedName("enquiry_id") var enquiryId: Int = 0,
    @SerializedName("created_by") var createdBy: Int = 0,
    @SerializedName("status") var status: String? = null,
    @SerializedName("follow_up_date") var followUpDate: String? = null,
    @SerializedName("comments") var comments: String? = null, ){

    var selectedDate : String = ""
    var selectedTime : String = ""
    companion object{
        const val ENQUIRY_STATUS_VIEWED = "Viewed"
    }
}