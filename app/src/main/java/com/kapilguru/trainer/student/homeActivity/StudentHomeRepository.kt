package com.kapilguru.trainer.student.homeActivity


import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.changePassword.model.LogoutRequest

class StudentHomeRepository(private val apiHelper: ApiHelper) {

    suspend fun logoutUser(logoutRequest : LogoutRequest) = apiHelper.logoutUser(logoutRequest)

    suspend fun getNotificationCount(userId : String) = apiHelper.getNotificationCount(userId)
}