package com.kapilguru.trainer.allSubscription

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.allSubscription.models.UpdateKycRequest

class AllSubscriptionRepository(private val apiHelper: ApiHelper) {
    suspend fun getAllSubscription() = apiHelper.getAllSubscriptions()
    suspend fun getMySubscriptions(trainerId : String) = apiHelper.getMySubscriptions(trainerId)
    suspend fun updateKyc(trainerId : String,updateKycRequest : UpdateKycRequest) = apiHelper.updateKyc(trainerId,updateKycRequest)
}