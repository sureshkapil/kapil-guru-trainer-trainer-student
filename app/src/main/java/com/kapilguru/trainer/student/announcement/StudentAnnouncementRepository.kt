package com.kapilguru.trainer.student.announcement

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.student.announcement.inbox.data.StudentLastMessageRequest
import com.kapilguru.trainer.student.announcement.newMessage.data.StudentSendAdminMessageRequest
import com.kapilguru.trainer.student.announcement.newMessage.data.StudentSendNewMessageRequest

class StudentAnnouncementRepository(private val apiHelper: ApiHelper) {

    suspend fun getBatchList(userId: String) = apiHelper.getBatchListForStudents(userId)

    suspend fun getAdminList() = apiHelper.getAdminListForStudent()

    suspend fun sendNewMessageRequest(request: StudentSendNewMessageRequest) = apiHelper.sendNewMessageReqByStudent(request)

    suspend fun sendAdminMessageRequest(request: StudentSendAdminMessageRequest) = apiHelper.sendAdminNewMessageReqByStudent(request)

    suspend fun getInBoxResponse(userId: String) = apiHelper.getInBoxResponseForStudent(userId)

    suspend fun getSentItemsResponse(userId: String) = apiHelper.getSentItemsResponseForStudent(userId)

    suspend fun updateLastMessageId(userId: String, studentLastMessageRequest: StudentLastMessageRequest) = apiHelper.updateLastMessageIdForStudent(userId, studentLastMessageRequest)

}