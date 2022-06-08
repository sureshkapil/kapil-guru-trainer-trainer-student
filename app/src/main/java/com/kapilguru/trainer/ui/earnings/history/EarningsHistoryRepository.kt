package com.kapilguru.trainer.ui.earnings.history

import com.kapilguru.trainer.ApiHelper

class EarningsHistoryRepository(private val apiHelper: ApiHelper) {

    suspend fun getEarningsHistory(trainerId: String) = apiHelper.getEarningsHistory(trainerId)

    suspend fun getHistoryDetailAmount(trainerId: String,selectedId: String) = apiHelper.getHistoryDetailAmount1(trainerId,selectedId)

//    http://35.174.62.203:9000/trainer/getRequestedDetails/5

}