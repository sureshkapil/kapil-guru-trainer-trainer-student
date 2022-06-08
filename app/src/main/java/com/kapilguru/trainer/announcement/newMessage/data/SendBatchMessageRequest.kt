package com.kapilguru.trainer.announcement.newMessage.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SendBatchMessageRequest(
    @SerializedName("reciever_id") var receiverid: String,
    @SerializedName("sender_id") var senderId: String,
    @SerializedName("subject") var subject: String,
    @SerializedName("message") var message: String,
    @SerializedName("reciever_type") var receiverType: String,
    @SerializedName("sender_type") var senderTye: String
)
