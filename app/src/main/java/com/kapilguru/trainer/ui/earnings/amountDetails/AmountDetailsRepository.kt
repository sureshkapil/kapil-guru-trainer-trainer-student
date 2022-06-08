package com.kapilguru.trainer.ui.earnings.amountDetails

import com.kapilguru.trainer.ApiHelper

class AmountDetailsRepository(private val apiHelper: ApiHelper) {

    suspend fun getearningDetailsList(trainerId: String)=apiHelper.getEarningsDetailsList(trainerId)

}