package com.kapilguru.trainer.forgotPassword.model

import com.google.gson.annotations.SerializedName

data class ValidateMobileResponse(
    @SerializedName("status") val status : Int,
    @SerializedName("message") val message : String,
    @SerializedName("data") val data : ValidateMobileResData)
