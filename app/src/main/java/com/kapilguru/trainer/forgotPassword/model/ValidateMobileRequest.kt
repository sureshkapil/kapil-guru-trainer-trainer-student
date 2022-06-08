package com.kapilguru.trainer.forgotPassword.model

import com.google.gson.annotations.SerializedName

data class ValidateMobileRequest(
    @SerializedName("contact_number") var contactNo : String? = null,
    @SerializedName("email_id") var emailId : String? = null,
)
