package com.kapilguru.trainer.signup.model.validateMail

import com.google.gson.annotations.SerializedName

data class ValidateMailResponse(
    @SerializedName("data") val validateMailData: ValidateMailResData,
    val message: String,
    val status: Int
)