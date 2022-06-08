package com.kapilguru.trainer.ui.earnings.webinarAmount

import com.kapilguru.trainer.ApiHelper

class WebinarAmountRepository(private val apiHelper: ApiHelper) {

    suspend fun getearningDetailsList(trainerId: String)=apiHelper.getEarningsDetailsList(trainerId)

}