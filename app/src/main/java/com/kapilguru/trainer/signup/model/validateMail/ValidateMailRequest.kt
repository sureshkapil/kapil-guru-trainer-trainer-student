package com.kapilguru.trainer.signup.model.validateMail

import com.google.gson.annotations.SerializedName

data class ValidateMailRequest(
    @SerializedName("email_id") var emailId: String = ""
)
