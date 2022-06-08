package com.kapilguru.trainer.ui.home

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.changePassword.model.LogoutRequest

class HomeScreenRepository(private val apiHelper: ApiHelper) {

     suspend  fun fetchUpcomingSchedule(trainerId: String) = apiHelper.fetchUpcomingSchedule(trainerId)

     suspend fun logoutUser(logoutRequest : LogoutRequest) = apiHelper.logoutUser(logoutRequest)

     suspend fun getNotificationCount(userId : String) = apiHelper.getNotificationCount(userId)

}