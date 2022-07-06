package com.kapilguru.trainer.enquiries.addOfflineEnquiry.data

import com.google.gson.annotations.SerializedName

data class AddEnquiryReq(
    @SerializedName("email_id") var emailId: String = "",
    @SerializedName("course_id") var courseId: String = "",
    @SerializedName("country_code") var countryCode: String = "",
    @SerializedName("full_name") var fullName: String = "",
    @SerializedName("uuid") var uuid: String = "",
    @SerializedName("created_by") var createdBy: Int = 0,
    @SerializedName("contact_number") var contactNumber: String = "",
    @SerializedName("trainer_id") var trainerId: Int = 0
)