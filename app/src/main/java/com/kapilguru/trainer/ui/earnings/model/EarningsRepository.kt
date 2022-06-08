package com.kapilguru.trainer.ui.earnings.model

import com.kapilguru.trainer.ApiHelper

class EarningsRepository(private val apiHelper: ApiHelper) {

    suspend fun getearningList(trainerId: String)=apiHelper.getEarningsList(trainerId)
}