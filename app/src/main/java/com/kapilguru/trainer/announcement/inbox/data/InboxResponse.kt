package com.kapilguru.trainer.announcement.inbox.data

import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.announcement.newMessage.data.NewMessageData

class InboxResponse(
    @SerializedName("data") val data: List<InboxItem> ? = null,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int)