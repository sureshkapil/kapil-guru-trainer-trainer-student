package com.kapilguru.trainer.announcement.newMessage.data

import com.google.gson.annotations.SerializedName

data class GetAdminListRes(
    @SerializedName("data") val adminList: List<AdminMessageData>? = null,
    @SerializedName("message") val message: String = "",
    @SerializedName("status") val status: Int = 0)