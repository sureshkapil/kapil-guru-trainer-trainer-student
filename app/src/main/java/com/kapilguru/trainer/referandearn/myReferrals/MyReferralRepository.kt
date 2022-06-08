package com.kapilguru.trainer.referandearn.myReferrals

import com.kapilguru.trainer.ApiHelper

class MyReferralRepository(private val apiHelper: ApiHelper) {
    suspend fun getMyReferrals(trainerId : String) = apiHelper.getMyReferrals(trainerId)
}