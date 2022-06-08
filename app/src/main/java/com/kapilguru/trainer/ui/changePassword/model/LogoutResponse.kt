package com.kapilguru.trainer.ui.changePassword.model

import com.google.gson.annotations.SerializedName

data class LogoutResponse(
    @SerializedName("data") val logoutResData: LogoutResponseData,
    @SerializedName("message") val message: String = "",
    @SerializedName("status") val status: Int = 0)