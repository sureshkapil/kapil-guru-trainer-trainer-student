package com.kapilguru.trainer.ui.earnings.viewModel

import com.kapilguru.trainer.ApiHelper

class EarningsDetailsRepository(private val apiHelper: ApiHelper) {

    suspend fun getearningDetailsList(trainerId: String)=apiHelper.getEarningsDetailsList(trainerId)

}