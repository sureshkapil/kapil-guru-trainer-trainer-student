package com.kapilguru.trainer.ui.refund.model

import com.kapilguru.trainer.ApiHelper

class RefundRepository(private val apiHelper: ApiHelper) {

    suspend fun getRefundList(trainerId: String)=apiHelper.getRefundList(trainerId)
}