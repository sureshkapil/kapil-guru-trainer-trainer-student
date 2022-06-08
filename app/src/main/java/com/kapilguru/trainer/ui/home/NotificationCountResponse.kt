package com.kapilguru.trainer.ui.home

import com.google.gson.annotations.SerializedName

data class NotificationCountResponse(
    @SerializedName("data") var notificationCountResponseApi: List<DataItem>?, @SerializedName("message") var message: String = "", @SerializedName("status") var status: Int = 0
)