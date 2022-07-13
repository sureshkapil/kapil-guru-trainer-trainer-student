package com.kapilguru.trainer.enquiries.todaysFollowUp.model

import com.google.gson.annotations.SerializedName

data class TodaysFollowUpResData(
    @SerializedName("email_id") var emailId: String = "",
    @SerializedName("follow_up_date") var followUpDate: String = "",
    @SerializedName("full_name") var fullName: String = "",
    @SerializedName("id") var id: Int = 0,
    @SerializedName("contact_number") var contactNumber: String = "",
    @SerializedName("status") var status: String = ""
)