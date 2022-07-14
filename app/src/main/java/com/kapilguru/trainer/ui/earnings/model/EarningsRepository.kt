package com.kapilguru.trainer.ui.earnings.model

import com.kapilguru.trainer.ApiHelper

class EarningsRepository(private val apiHelper: ApiHelper) {

    suspend fun getearningList(trainerId: String)=apiHelper.getEarningsList(trainerId)

    suspend fun getEarningData(trainerId: String)=apiHelper.getEarningData(trainerId)

    suspend fun getEarningDetails(trainerId: String)=apiHelper.getEarningDetails(trainerId)
}