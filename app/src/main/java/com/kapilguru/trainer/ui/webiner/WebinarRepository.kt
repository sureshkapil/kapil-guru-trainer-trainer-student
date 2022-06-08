package com.kapilguru.trainer.ui.webiner

import com.kapilguru.trainer.ApiHelper

class WebinarRepository(private val apiHelper: ApiHelper) {

    suspend fun getWebinarList(trainerId: String)=apiHelper.getWebinarList(trainerId)

    suspend fun getLiveUpComingWebinarList(trainerId: String)=apiHelper.getLiveUpComingWebinarList(trainerId)

    suspend fun deleteWebinar(webinarId: String)= apiHelper.deleteWebinar(webinarId)

    suspend fun fetchWebinarDetails(webinarId: String) = apiHelper.fetchWebinarDetails(webinarId)
}