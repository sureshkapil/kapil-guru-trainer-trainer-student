package com.kapilguru.trainer.signup.model.validateMail

import com.google.gson.annotations.SerializedName

data class ValidateMailResData(
    @SerializedName("email_count") val emailCount: Int
)