package com.kapilguru.trainer.signup.model.register

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("data")val registerResData: List<RegisterResData>,
    val message: String,
    val status: Int
)