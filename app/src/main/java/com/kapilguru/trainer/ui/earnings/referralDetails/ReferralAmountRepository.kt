package com.kapilguru.trainer.ui.earnings.referralDetails

import com.kapilguru.trainer.ApiHelper

class ReferralAmountRepository(private val apiHelper: ApiHelper) {

    suspend fun getearningDetailsList(trainerId: String)=apiHelper.getEarningsDetailsList(trainerId)

}