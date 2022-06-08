package com.kapilguru.trainer.announcement.sentItems.data

import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.announcement.inbox.data.InboxItem

data class SentItemsResponse(
    @SerializedName("data") val data: List<SentItemsData>? = null,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)
