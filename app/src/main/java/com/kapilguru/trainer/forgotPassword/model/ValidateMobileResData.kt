package com.kapilguru.trainer.forgotPassword.model

import com.google.gson.annotations.SerializedName

data class ValidateMobileResData(
    @SerializedName("contact_count") var contact_count : Int
)
