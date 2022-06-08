package com.kapilguru.trainer.ui.reports

import com.kapilguru.trainer.ApiHelper

class ReportsRepository(private val apiHelper: ApiHelper) {
    suspend fun getCousesList(trainerId : String) = apiHelper.getCousesList(trainerId)
    suspend fun getWebinarList(trainerId: String)=apiHelper.getWebinarList(trainerId)
    suspend fun getGuestLectureList(trainerId: String)=apiHelper.getGuestlectureList(trainerId)
}