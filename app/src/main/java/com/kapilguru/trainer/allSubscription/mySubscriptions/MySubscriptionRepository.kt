package com.kapilguru.trainer.allSubscription.mySubscriptions

import com.kapilguru.trainer.ApiHelper

class MySubscriptionRepository(private val apiHepler : ApiHelper) {
//    Api call is not made here
    suspend fun getMySubscriptions(trainerId : String) = apiHepler.getMySubscriptions(trainerId)
}