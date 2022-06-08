package com.kapilguru.trainer.announcement

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.announcement.newMessage.data.SendAdminMessageRequest
import com.kapilguru.trainer.announcement.newMessage.data.SendBatchMessageRequest
import com.kapilguru.trainer.announcement.sentItems.data.LastMessageRequest

class AnnouncementRepository(private val apiHelper: ApiHelper) {
    suspend fun getBatchList(userId: String) = apiHelper.getBatchList(userId)

    suspend fun getAdminList() = apiHelper.getAdminList()

    suspend fun sendBatchMessageRequest(request : SendBatchMessageRequest) = apiHelper.sendBatchNewMessageReq(request)

    suspend fun sendAdminMessageRequest(request : SendAdminMessageRequest) = apiHelper.sendAdminNewMessageReq(request)

    suspend fun getInboxResponce(userId : String) = apiHelper.getInboxResponce(userId)

    suspend fun getSentItemsResponce(userId : String) = apiHelper.getSentItemsResponce(userId)

    suspend fun updateLastMessageId(userId : String,lastMessageRequest: LastMessageRequest) = apiHelper.updateLastMessageId(userId,lastMessageRequest)

}