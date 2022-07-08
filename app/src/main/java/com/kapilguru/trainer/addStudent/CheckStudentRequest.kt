package com.kapilguru.trainer.addStudent

import com.google.gson.annotations.SerializedName

data class CheckStudentRequest(
    @SerializedName("email_id") var emailId: String? = "", @SerializedName("contact_number") var contactNumber: String? = ""
)